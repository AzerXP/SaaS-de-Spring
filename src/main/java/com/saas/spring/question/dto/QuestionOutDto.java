package com.saas.spring.question.dto;

public record QuestionOutDto(
        Long id,
        String text,
        Long questionTypeId
) {
}

