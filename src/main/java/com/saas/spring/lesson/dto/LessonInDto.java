package com.saas.spring.lesson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LessonInDto(
        @NotBlank(message = "El título de la lección no puede ser nulo o vacío")
        String title,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String description
) {
}
