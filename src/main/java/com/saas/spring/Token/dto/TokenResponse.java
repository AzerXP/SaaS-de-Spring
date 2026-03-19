package com.saas.spring.Token.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
    @JsonProperty("access_token")
    String accesToken,
    @JsonProperty("message")
    String message
) {
}

