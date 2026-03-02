package com.saas.spring.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseInDto(
        @NotBlank(message = "El título del curso no puede ser nulo o vacío")
        String title,

        @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
        String description
) {
}
