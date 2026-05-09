package com.quiniela.application;

import com.quiniela.ports.in.LoginUserUseCase;
import com.quiniela.ports.out.PasswordHasher;
import com.quiniela.ports.out.TokenIssuer;
import com.quiniela.ports.out.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LoginUserServiceTest {

    @Test
    public void shouldLoginUserSuccessfully() {

        // Arrange - Given
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var userId = UUID.randomUUID();

        userRepository.save(new UserRepository.UserRecord(
                userId,
                "Miguel",
                "miguel@email.com",
                "hashed_password123"
        ));

        var service = new LoginUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new LoginUserUseCase.Command(
                "miguel@email.com",
                "password123"
        );

        // Act - When
        var result = service.login(command);

        // Assert - Then
        assertNotNull(result);
        assertEquals(userId, result.userId());
        assertEquals("fake-token", result.accessToken());
    }

    private static class FakePasswordHasher implements PasswordHasher {

        @Override
        public String hash(String rawPassword) {
            return "hashed_" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String hashedPassword) {
            return hashedPassword.equals("hashed_" + rawPassword);
        }
    }

    @Test
    public void shouldRejectUnknownEmail() {

        // Arrange - Given
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var service = new LoginUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new LoginUserUseCase.Command(
                "unknown@email.com",
                "password123"
        );

        // Act - When + Assert - Then
        assertThrows(IllegalArgumentException.class, () -> service.login(command));
    }

    @Test
    public void shouldRejectInvalidPassword() {

        // Arrange
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var userId = UUID.randomUUID();

        userRepository.save(new UserRepository.UserRecord(
                userId,
                "Miguel",
                "miguel@email.com",
                "hashed_password123"
        ));

        var service = new LoginUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new LoginUserUseCase.Command(
                "miguel@email.com",
                "wrong-password"
        );

        // Act - When + Assert - Then
        assertThrows(IllegalArgumentException.class, () -> service.login(command));
    }

    private static class InMemoryUserRepository implements UserRepository {

        private final Map<String, UserRecord> usersByEmail = new HashMap<>();

        @Override
        public Optional<UserRecord> findByEmail(String email) {
            return Optional.ofNullable(usersByEmail.get(email));
        }

        @Override
        public UUID save(UserRecord user) {
            usersByEmail.put(user.email(), user);
            return user.id();
        }
    }
}
