package com.quiniela.adapters.in.web;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String accessToken
) {
}
