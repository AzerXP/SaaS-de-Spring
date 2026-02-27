package com.saas.spring.questionConfig.dto;
import java.util.Map;

public record QuestionConfigOutDto(
    Long questionId,

    Map<String, Object> config
) {
}
