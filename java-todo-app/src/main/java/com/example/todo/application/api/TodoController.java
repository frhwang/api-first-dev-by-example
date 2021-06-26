package com.example.todo.application.api;

import com.example.todo.application.dto.TodoRequest;
import com.example.todo.application.dto.TodoResponse;
import com.example.todo.application.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos() {
        return ResponseEntity.ok(todoService.getTodos());
    }

    @PostMapping
    public ResponseEntity<TodoResponse> saveTodo(@RequestBody TodoRequest todoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(todoService.saveTodo(todoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
