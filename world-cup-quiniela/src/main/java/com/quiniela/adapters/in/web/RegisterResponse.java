package com.quiniela.adapters.in.web;

import java.util.UUID;

public record RegisterResponse (
        UUID userId,
        String accessToken
) {
}
