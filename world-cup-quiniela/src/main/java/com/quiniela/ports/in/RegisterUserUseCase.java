package com.quiniela.ports.in;

import java.util.UUID;

public interface RegisterUserUseCase {
    Result register(Command command);

    record Command(
            String displayName,
            String email,
            String password,
            String confirmPassword) {}

    record Result(UUID userId,
                  String accessToken) {}
}
