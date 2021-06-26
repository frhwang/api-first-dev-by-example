package com.example.todo.application.service

import com.example.todo.application.dto.TodoRequest
import com.example.todo.application.dto.TodoResponse

interface TodoService {
    fun getTodos(): List<TodoResponse>
    fun saveTodo(todoRequest: TodoRequest): TodoResponse
    fun deleteTodo(id: Long)
}
