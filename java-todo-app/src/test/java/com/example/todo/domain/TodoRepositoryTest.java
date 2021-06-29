package com.example.todo.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void save_and_get_todo() {
        Todo todoToSave = Todo.of("어떤 할 일");
        Todo savedTodo = todoRepository.save(todoToSave);

        assertThat(savedTodo).isEqualTo(todoToSave);

        Todo fetchedTodo = todoRepository.findById(savedTodo.getId()).orElseThrow();

        assertThat(savedTodo).isEqualTo(fetchedTodo);
    }

    @Test
    public void save_and_delete_todo() {
        Todo todoToSave = Todo.of("어떤 할 일");
        Todo savedTodo = todoRepository.save(todoToSave);
        Long id = savedTodo.getId();

        assertThat(todoRepository.existsById(id)).isTrue();

        todoRepository.deleteById(savedTodo.getId());

        assertThat(todoRepository.existsById(id)).isFalse();
    }
}
