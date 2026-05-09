package com.quiniela.ports.out;

import java.util.UUID;

public interface TokenIssuer {
    String issueAccessToken(UUID userId, String email);
}
