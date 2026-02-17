package com.saas.spring.Role.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleInDto(
    @NotBlank(message = "Error el nombre no puede nulo o vacio")
    String nombre
) {
}
