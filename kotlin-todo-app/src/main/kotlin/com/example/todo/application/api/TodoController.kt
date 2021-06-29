package com.example.todo.application.api

import com.example.todo.application.dto.TodoRequest
import com.example.todo.application.dto.TodoResponse
import com.example.todo.application.service.TodoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todos")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun getTodos(): ResponseEntity<List<TodoResponse>> =
        ResponseEntity.ok(todoService.getTodos())

    @PostMapping
    fun postTodo(@RequestBody todoRequest: TodoRequest): ResponseEntity<TodoResponse> =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(todoService.saveTodo(todoRequest))

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long): ResponseEntity<Unit> {
        todoService.deleteTodo(id)
        return ResponseEntity.noContent().build()
    }
}
