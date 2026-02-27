package com.saas.spring.questionConfig.dto;

import java.util.Map;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;

public record QuestionConfigUpdateDto(
    @Nullable
    @Positive(message = "El ID de la pregunta debe ser positivo")
    Long questionId,

    @Nullable
    Map<String, Object> config
) {

}
