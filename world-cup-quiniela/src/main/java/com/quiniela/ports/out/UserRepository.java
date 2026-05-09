package com.quiniela.ports.out;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<UserRecord> findByEmail(String email);
    UUID save(UserRecord user);

    record UserRecord(UUID id,
                      String displayName,
                      String email,
                      String passwordHash) {}
}
