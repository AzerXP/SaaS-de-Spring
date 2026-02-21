package com.saas.spring.questionType.dto;

import jakarta.annotation.Nullable;

public record QuestionTypeUpdateDto(
    @Nullable
    String name
) {

}
