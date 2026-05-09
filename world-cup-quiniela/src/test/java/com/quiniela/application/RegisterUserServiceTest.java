package com.quiniela.application;

import com.quiniela.ports.in.RegisterUserUseCase;
import com.quiniela.ports.out.PasswordHasher;
import com.quiniela.ports.out.TokenIssuer;
import com.quiniela.ports.out.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserServiceTest {

    @Test
    public void shouldRegisterUserSuccessfully() {
        // Arrange - Given
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var service = new RegisterUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new RegisterUserUseCase.Command(
                "Miguel",
                "miguel@email.com",
                "password123",
                "password123"
        );

        // Act - When
        var result = service.register(command);

        // Assert - Then
        assertNotNull(result);
        assertNotNull(result.userId());
        assertEquals("fake-token", result.accessToken());

        var savedUser = userRepository.findByEmail("miguel@email.com");

        assertTrue(savedUser.isPresent());
        assertEquals("Miguel", savedUser.get().displayName());
        assertEquals("miguel@email.com", savedUser.get().email());
        assertEquals("hashed_password123", savedUser.get().passwordHash());

    }

    @Test
    public void shouldRejectDuplicateEmail() {

        // Arrange - Given
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var service = new RegisterUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new RegisterUserUseCase.Command(
                "Miguel",
                "miguel@email.com",
                "password123",
                "password123"
        );

        userRepository.save(new UserRepository.UserRecord(
                UUID.randomUUID(),
                "Miguel",
                "miguel@email.com",
                "hashed_password123"
        ));

        // Act - When + Assert - Then
        assertThrows(IllegalStateException.class, () -> {
            service.register(command);
        });
    }

    @Test
    public void shouldRejectShortPassword() {
        // Arrange - Given
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var service = new RegisterUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new RegisterUserUseCase.Command(
                "Miguel",
                "miguel@email.com",
                "123",
                "123"
        );

        // Act - When + Assert - Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.register(command);
        });
    }

    @Test
    public void shouldRejectPasswordMismatch() {

        //Arrange - Given
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var service = new RegisterUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new RegisterUserUseCase.Command(
                "Miguel",
                "miguel@email.com",
                "password123",
                "differentPassword"
        );

        // Act - When + Assert - Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.register(command);
        });
    }

    @Test
    public void shouldNormalizeEmailToLowerCase() {
        //Arrange - Given
        var userRepository = new InMemoryUserRepository();
        PasswordHasher passwordHasher = new FakePasswordHasher();
        TokenIssuer tokenIssuer = (userId,email) -> "fake-token";

        var service = new RegisterUserService(
                userRepository,
                passwordHasher,
                tokenIssuer
        );

        var command = new RegisterUserUseCase.Command(
                "Miguel",
                "Miguel@Email.COM",
                "password123",
                "password123"
        );

        // Act - When
        service.register(command);

        // Assert - Then
        var savedUser = userRepository.findByEmail("miguel@email.com");

        assertTrue(savedUser.isPresent());
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
}
