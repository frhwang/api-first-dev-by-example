package com.example.todo.application.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class TodoResponse {
    private final Long id;
    private final String content;
}
