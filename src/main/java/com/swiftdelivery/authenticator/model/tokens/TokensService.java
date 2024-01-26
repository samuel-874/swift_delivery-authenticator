package com.swiftdelivery.authenticator.model.tokens;

import org.springframework.stereotype.Service;

@Service
public interface TokensService {

    String generateToken(String email);

    void deleteToken(String reference, String token);

    Boolean validateToken(String reference,String token);
}
