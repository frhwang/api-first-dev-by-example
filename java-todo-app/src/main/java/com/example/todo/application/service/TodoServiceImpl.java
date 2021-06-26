package com.example.todo.application.service;

import com.example.todo.application.dto.TodoRequest;
import com.example.todo.application.dto.TodoResponse;
import com.example.todo.domain.Todo;
import com.example.todo.domain.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> getTodos() {
        return todoRepository.findAll().stream()
            .map(todo -> TodoResponse.of(todo.getId(), todo.getContent()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public TodoResponse saveTodo(TodoRequest todoRequest) {
        Todo savedTodo = todoRepository.save(Todo.of(todoRequest.getContent()));
        return TodoResponse.of(savedTodo.getId(), savedTodo.getContent());
    }

    @Override
    @Transactional
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
