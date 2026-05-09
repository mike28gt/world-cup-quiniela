package com.quiniela.application;

import com.quiniela.ports.in.LoginUserUseCase;
import com.quiniela.ports.out.PasswordHasher;
import com.quiniela.ports.out.TokenIssuer;
import com.quiniela.ports.out.UserRepository;

public class LoginUserService implements LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public LoginUserService(UserRepository userRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public Result login(Command command) {

        String email = normalizeEmail(command.email());

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordHasher.matches(command.password(), user.passwordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = tokenIssuer.issueAccessToken(user.id(), user.email());

        return new Result(user.id(), token);
    }

    private String normalizeEmail(String email) {

        if (email == null || email.trim().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        return email.trim().toLowerCase();
    }
}
