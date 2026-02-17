package com.saas.spring.User.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserInDto(
    @NotBlank(message = "El nombre no puede ser nulo vacio")
    String nombre,
    @NotBlank(message = "El password no puede ser nulo o vacio")
    String password,
    @NotNull(message = "El id del role no puede ser nulo")
    List<Long> IdRoles
) {

}
