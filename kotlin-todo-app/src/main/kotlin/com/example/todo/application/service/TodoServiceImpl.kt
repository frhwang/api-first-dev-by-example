package com.example.todo.application.service

import com.example.todo.application.dto.TodoRequest
import com.example.todo.application.dto.TodoResponse
import com.example.todo.domain.Todo
import com.example.todo.domain.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TodoServiceImpl(private val todoRepository: TodoRepository) : TodoService {

    @Transactional(readOnly = true)
    override fun getTodos(): List<TodoResponse> =
        todoRepository.findAll()
            .map { TodoResponse(it.id, it.content) }

    @Transactional
    override fun saveTodo(todoRequest: TodoRequest): TodoResponse =
        todoRepository.save(Todo.of(todoRequest.content))
            .let { TodoResponse(it.id, it.content) }

    @Transactional
    override fun deleteTodo(id: Long) =
        todoRepository.deleteById(id)
}
