package com.saas.spring.questionConfig.dto;

import java.util.Map;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuestionConfigInDto(  
    @NotNull(message = "El ID de la pregunta es requerido")
    @Positive(message = "El ID de la pregunta debe ser positivo")
    Long questionId,

    @Nullable
    Map<String, Object> config
) {

}
