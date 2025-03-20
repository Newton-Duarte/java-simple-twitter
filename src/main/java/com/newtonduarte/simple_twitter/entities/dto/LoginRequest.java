package com.newtonduarte.simple_twitter.entities.dto;

public record LoginRequest(
        String name,
        String password
) {
}
