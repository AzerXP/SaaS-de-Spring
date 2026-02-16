package com.saas.spring.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QuestionInDto(
        @NotBlank(message = "La pregunta no puede ser vacía")
        @Size(min = 8, message = "El texto de la pregunta debe ser más grande")
        String text
) {
}
