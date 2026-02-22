package com.saas.spring.question.dto;

import jakarta.annotation.Nullable;

public record QuestionUpdateDto(
        @Nullable
        String text,
        @Nullable
        Long questionTypeId
) {
}
