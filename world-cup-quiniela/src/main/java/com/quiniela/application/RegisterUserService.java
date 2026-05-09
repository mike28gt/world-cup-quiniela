package com.quiniela.application;

import com.quiniela.ports.in.RegisterUserUseCase;
import com.quiniela.ports.out.PasswordHasher;
import com.quiniela.ports.out.TokenIssuer;
import com.quiniela.ports.out.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public RegisterUserService(UserRepository userRepository, PasswordHasher passwordHasher, TokenIssuer tokenIssuer) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public Result register(Command command) {
        String displayName = requireNonBlank(command.displayName(), "Display name is required");
        String email = normalizeEmail(requireNonBlank(command.email(), "Email is required"));

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        UUID userId = UUID.randomUUID();
        String password = command.password();
        String confirmPassword = command.confirmPassword();

        validatePassword(password, confirmPassword);

        String passwordHash = passwordHasher.hash(command.password());

        userRepository.save(new UserRepository.UserRecord(
                userId,
                displayName,
                email,
                passwordHash
        ));

        String token = tokenIssuer.issueAccessToken(userId, email);

        return new Result(userId, token);
    }

    private String requireNonBlank(String value, String message) {

        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private String normalizeEmail(String email) {

        if (email == null || email.trim().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        return email.trim().toLowerCase();
    }

    private void validatePassword(String password, String confirmPassword) {

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must have at least 8 characters");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
    }
}
