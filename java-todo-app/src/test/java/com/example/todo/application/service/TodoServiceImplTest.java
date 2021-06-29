package com.example.todo.application.service;

import com.example.todo.application.dto.TodoRequest;
import com.example.todo.application.dto.TodoResponse;
import com.example.todo.domain.Todo;
import com.example.todo.domain.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    public void getTodos() {
        List<Todo> mockTodos = Arrays.asList(
            Todo.of(1L, "할 일 1"),
            Todo.of(2L, "할 일 2")
        );
        when(todoRepository.findAll()).thenReturn(mockTodos);

        List<TodoResponse> todos = todoService.getTodos();

        assertThat(todos).hasSize(mockTodos.size());
        assertThat(todos).contains(TodoResponse.of(mockTodos.get(0).getId(), mockTodos.get(0).getContent()));
        verify(todoRepository).findAll();
    }

    @Test
    public void saveTodo() {
        Todo todoToSave = Todo.of("할 일");
        Todo savedTodo = Todo.of(1L, todoToSave.getContent());
        when(todoRepository.save(todoToSave)).thenReturn(savedTodo);

        TodoRequest todoRequest = TodoRequest.of(todoToSave.getContent());
        TodoResponse todoResponse = todoService.saveTodo(todoRequest);

        assertThat(todoResponse).isNotNull();
        assertThat(todoResponse).isEqualTo(TodoResponse.of(savedTodo.getId(), savedTodo.getContent()));
        verify(todoRepository).save(todoToSave);
    }

    @Test
    public void deleteTodo() {
        Long id = 1L;

        todoService.deleteTodo(id);

        verify(todoRepository).deleteById(id);
    }
}
