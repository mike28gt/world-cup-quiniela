package com.quiniela.ports.out;

import java.util.UUID;

public interface TokenValidator {
    TokenData validate(String token);

    record TokenData(
            UUID userId,
            String email
    ) {
    }
}
