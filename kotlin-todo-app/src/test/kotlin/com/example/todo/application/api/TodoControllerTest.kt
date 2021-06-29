package com.example.todo.application.api

import com.example.todo.application.dto.TodoRequest
import com.example.todo.application.dto.TodoResponse
import com.example.todo.application.service.TodoService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
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
    fun `할 일 목록 조회`() {
        val todos = listOf(
            TodoResponse(1L, "할 일 1"),
            TodoResponse(2L, "할 일 2")
        )

        `when`(todoService.getTodos()).thenReturn(todos)

        mockMvc
            .perform(get("/todos").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(todos)))
            .andDo(
                document(
                    "get-todos",
                    responseFields(
                        fieldWithPath("[]").description("할 일 목록")
                    ).andWithPrefix(
                        "[]",
                        fieldWithPath("id").description("할 일 ID"),
                        fieldWithPath("content").description("할 일 내용"),
                    )
                )
            )
    }

    @Test
    fun `할 일 등록`() {
        val todoRequest = TodoRequest("할 일")
        val todoResponse = TodoResponse(1L, todoRequest.content)

        `when`(todoService.saveTodo(todoRequest)).thenReturn(todoResponse)

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
                        fieldWithPath("content").description("할 일 내용")
                    ),
                    responseFields(
                        fieldWithPath("id").description("할 일 ID"),
                        fieldWithPath("content").description("할 일 내용"),
                    )
                )
            )
    }

    @Test
    fun `할 일 삭제`() {
        val id = 1L

        mockMvc
            .perform(delete("/todos/{id}", id).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "delete-todo",
                    pathParameters(
                        parameterWithName("id").description("할 일 ID")
                    )
                )
            )

        verify(todoService).deleteTodo(id)
    }
}
