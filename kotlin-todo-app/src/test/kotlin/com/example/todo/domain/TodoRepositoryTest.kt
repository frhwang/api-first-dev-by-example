package com.example.todo.domain

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @Test
    fun `Todo는 영속 및 조회가 가능하다`() {
        val todoToSave = Todo.of("할 일")
        val savedTodo = todoRepository.save(todoToSave)

        assertThat(savedTodo, `is`(equalTo(todoToSave)))

        val fetchedTodo = todoRepository.findByIdOrNull(savedTodo.id)

        assertThat(fetchedTodo, `is`(notNullValue()))
        assertThat(fetchedTodo, `is`(equalTo(savedTodo)))
    }

    @Test
    fun `영속된 Todo는 삭제할 수 있다`() {
        val savedTodo = todoRepository.save(Todo.of("할 일"))

        assertThat(todoRepository.existsById(savedTodo.id), `is`(true))

        todoRepository.deleteById(savedTodo.id)

        assertThat(todoRepository.existsById(savedTodo.id), `is`(false))
    }
}
