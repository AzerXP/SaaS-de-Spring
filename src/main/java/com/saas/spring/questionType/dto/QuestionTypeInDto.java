package com.saas.spring.questionType.dto;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionTypeInDto(
    @NotBlank(message = "La pregunta no puede ser vacía")
    String name,

    @NotNull
    Map<String, Object> config_schema
) {

}
