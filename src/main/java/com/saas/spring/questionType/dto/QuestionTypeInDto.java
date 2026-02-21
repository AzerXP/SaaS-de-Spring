package com.saas.spring.questionType.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionTypeInDto(
    @NotBlank(message = "La pregunta no puede ser vacía")
    String name
) {

}
