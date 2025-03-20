package com.newtonduarte.simple_twitter.entities.dto;

public record LoginResponse(
        String accessToken,
        Long expiresIn
) {
}
