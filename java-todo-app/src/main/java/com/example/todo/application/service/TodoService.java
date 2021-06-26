package com.example.todo.application.service;

import com.example.todo.application.dto.TodoRequest;
import com.example.todo.application.dto.TodoResponse;

import java.util.List;

public interface TodoService {
    List<TodoResponse> getTodos();

    TodoResponse saveTodo(TodoRequest todoRequest);

    void deleteTodo(Long id);
}
