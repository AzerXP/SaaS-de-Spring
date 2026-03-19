package com.saas.spring.Token.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "El nombre de usuario no puede ser nulo")
    String nombre,

    @NotBlank(message = "La contraseña no puede ser nula")
    String password
) {

}
