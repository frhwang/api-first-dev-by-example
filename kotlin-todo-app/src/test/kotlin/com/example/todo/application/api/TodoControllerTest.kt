package com.example.todo.application.api

import com.example.todo.application.dto.TodoRequest
import com.example.todo.application.dto.TodoResponse
import com.example.todo.application.service.TodoService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@WebMvcTest(TodoController::class)
@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
class TodoControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var todoService: TodoService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp(webApplicationContext: WebApplicationContext, contextProvider: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                documentationConfiguration(contextProvider)
                    .operationPreprocessors()
                    .withResponseDefaults(prettyPrint())
            )
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    @Test
    fun `??? ??? ?????? ??????`() {
        val todos = listOf(
            TodoResponse(1L, "??? ??? 1"),
            TodoResponse(2L, "??? ??? 2")
        )

        whenever(todoService.getTodos()).thenReturn(todos)

        mockMvc
            .perform(get("/todos").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(todos)))
            .andDo(
                document(
                    "get-todos",
                    responseFields(
                        fieldWithPath("[]").description("??? ??? ??????")
                    ).andWithPrefix(
                        "[]",
                        fieldWithPath("id").description("??? ??? ID"),
                        fieldWithPath("content").description("??? ??? ??????"),
                    )
                )
            )
    }

    @Test
    fun `??? ??? ??????`() {
        val todoRequest = TodoRequest("??? ???")
        val todoResponse = TodoResponse(1L, todoRequest.content)

        whenever(todoService.saveTodo(todoRequest)).thenReturn(todoResponse)

        mockMvc
            .perform(
                post("/todos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(todoRequest))
            )
            .andExpect(status().isCreated)
            .andExpect(content().json(objectMapper.writeValueAsString(todoResponse)))
            .andDo(
                document(
                    "save-todo",
                    requestFields(
                        fieldWithPath("content").description("??? ??? ??????")
                    ),
                    responseFields(
                        fieldWithPath("id").description("??? ??? ID"),
                        fieldWithPath("content").description("??? ??? ??????"),
                    )
                )
            )
    }

    @Test
    fun `??? ??? ??????`() {
        val id = 1L

        mockMvc
            .perform(delete("/todos/{id}", id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "delete-todo",
                    pathParameters(
                        parameterWithName("id").description("??? ??? ID")
                    )
                )
            )

        verify(todoService).deleteTodo(id)
    }
}
