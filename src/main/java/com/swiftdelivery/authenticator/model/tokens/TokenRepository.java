package com.swiftdelivery.authenticator.model.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Tokens, Long> {
    Tokens findByReference(String email);

    Optional<Tokens> findByReferenceAndToken(String reference, String token);
}
