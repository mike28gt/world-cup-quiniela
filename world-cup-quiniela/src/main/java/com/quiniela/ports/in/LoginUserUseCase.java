package com.quiniela.ports.in;

import java.util.UUID;

public interface LoginUserUseCase {

    Result login(Command command);

    record Command(
            String email,
            String password
    ) {}

    record Result(
            UUID userId,
            String accessToken
    ) {
    }
}
