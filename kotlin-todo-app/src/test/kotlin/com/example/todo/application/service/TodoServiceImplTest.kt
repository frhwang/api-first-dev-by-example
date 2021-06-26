package com.example.todo.application.service

import com.example.todo.application.dto.TodoRequest
import com.example.todo.application.dto.TodoResponse
import com.example.todo.domain.Todo
import com.example.todo.domain.TodoRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class TodoServiceImplTest {

    @Mock
    private lateinit var todoRepository: TodoRepository

    @InjectMocks
    private lateinit var todoServiceImpl: TodoServiceImpl

    @Test
    fun getTodos() {
        val mockTodos = listOf(
            Todo(1L, "할 일 1"),
            Todo(2L, "할 일 2"),
        )

        `when`(todoRepository.findAll()).thenReturn(mockTodos)

        val todos = todoServiceImpl.getTodos()

        assertThat(todos, hasSize(mockTodos.size))
        assertThat(todos, hasItem(mockTodos.random().let { TodoResponse(it.id, it.content) }))

        verify(todoRepository).findAll()
    }

    @Test
    fun saveTodo() {
        val content = "할 일"
        val id = 1L
        val todoToSave = Todo.of(content)

        `when`(todoRepository.save(todoToSave)).thenReturn(todoToSave.copy(id = id))

        val todoResponse = todoServiceImpl.saveTodo(TodoRequest(content))

        assertThat(todoResponse.id, `is`(id))
        assertThat(todoResponse.content, `is`(content))

        verify(todoRepository).save(todoToSave)
    }

    @Test
    fun deleteTodo() {
        val id = 1L

        todoServiceImpl.deleteTodo(id)

        verify(todoRepository).deleteById(id)
    }
}
