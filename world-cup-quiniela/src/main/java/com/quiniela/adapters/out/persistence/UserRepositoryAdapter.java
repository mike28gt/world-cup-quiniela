package com.quiniela.adapters.out.persistence;

import com.quiniela.ports.out.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpa;

    public UserRepositoryAdapter(JpaUserRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<UserRecord> findByEmail(String email) {
        return jpa.findByEmail(email).map(e ->
                new UserRecord(e.getId(), e.getDisplayName(), e.getEmail(), e.getPasswordHash()));
    }

    @Override
    public UUID save(UserRecord user) {
        var now = LocalDateTime.now();

        var entity = new JpaUserEntity(
                user.id(),
                user.displayName(),
                user.email(),
                user.passwordHash(),
                now,
                now
        );

        return jpa.save(entity).getId();
    }
}
