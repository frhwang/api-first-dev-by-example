package com.example.todo.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void save_and_get_todo() {
        Todo todoToSave = Todo.of("어떤 할 일");
        Todo savedTodo = todoRepository.save(todoToSave);

        assertThat(savedTodo, is(equalTo(todoToSave)));

        Optional<Todo> fetchedTodo = todoRepository.findById(savedTodo.getId());

        assertThat(fetchedTodo.isPresent(), is(true));
        assertThat(savedTodo, is(equalTo(fetchedTodo.get())));
    }

    @Test
    public void save_and_delete_todo() {
        Todo todoToSave = Todo.of("어떤 할 일");
        Todo savedTodo = todoRepository.save(todoToSave);
        Long id = savedTodo.getId();

        assertThat(todoRepository.existsById(id), is(true));

        todoRepository.deleteById(savedTodo.getId());

        assertThat(todoRepository.existsById(id), is(false));
    }
}
