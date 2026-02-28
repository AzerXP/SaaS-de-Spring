package com.saas.spring.questionType.dto;

import java.util.Map;

public record QuestionTypeOutDto(
    Long id,
    String name,
    Map<String, Object> config_schema
) {

}
