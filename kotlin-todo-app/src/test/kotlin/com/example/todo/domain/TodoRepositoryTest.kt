package com.example.todo.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @Test
    fun `Todo는 영속 및 조회가 가능하다`() {
        val todoToSave = Todo.of("할 일")
        val savedTodo = todoRepository.save(todoToSave)

        assertThat(savedTodo).isEqualTo(todoToSave)

        val fetchedTodo = todoRepository.findById(savedTodo.id).orElseThrow()

        assertThat(fetchedTodo).isEqualTo(savedTodo)
    }

    @Test
    fun `영속된 Todo는 삭제할 수 있다`() {
        val savedTodo = todoRepository.save(Todo.of("할 일"))

        assertThat(todoRepository.existsById(savedTodo.id)).isTrue()

        todoRepository.deleteById(savedTodo.id)

        assertThat(todoRepository.existsById(savedTodo.id)).isFalse()
    }
}
