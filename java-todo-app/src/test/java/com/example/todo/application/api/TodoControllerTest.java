package com.example.todo.application.api;

import com.example.todo.application.dto.TodoRequest;
import com.example.todo.application.dto.TodoResponse;
import com.example.todo.application.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class TodoControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider contextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(contextProvider)
                .operationPreprocessors()
                .withResponseDefaults(prettyPrint()))
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();
    }

    @Test
    public void getTodos() throws Exception {
        List<TodoResponse> response = List.of(
            TodoResponse.of(1L, "?????????"),
            TodoResponse.of(2L, "????????? ??????")
        );

        when(todoService.getTodos()).thenReturn(response);

        mockMvc.perform(get("/todos").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)))
            .andDo(document("{method-name}",
                responseFields(fieldWithPath("[]").description("??? ??? ??????"))
                    .andWithPrefix("[]",
                        fieldWithPath("id").description("??? ??? ID"),
                        fieldWithPath("content").description("??? ??? ??????")
                    )
            ));
    }

    @Test
    public void saveTodo() throws Exception {
        TodoRequest request = TodoRequest.of("?????? ??? ???");
        TodoResponse response = TodoResponse.of(1L, request.getContent());

        when(todoService.saveTodo(request)).thenReturn(response);

        mockMvc.perform(post("/todos").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(response)))
            .andDo(document("{method-name}",
                requestFields(
                    fieldWithPath("content").description("??? ??? ??????")
                ),
                responseFields(
                    fieldWithPath("id").description("??? ??? ID"),
                    fieldWithPath("content").description("??? ??? ??????")
                )
            ));
    }

    @Test
    public void deleteTodo() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/todos/{id}", id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("{method-name}",
                pathParameters(
                    parameterWithName("id").description("??? ??? ID")
                )
            ));

        verify(todoService).deleteTodo(id);
    }
}
