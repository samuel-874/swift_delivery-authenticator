package com.swiftdelivery.authenticator.model.tokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TokensServiceImpl implements TokensService {

    @Autowired
    private TokenRepository tokenRepo;

    @Override
    public String generateToken(String email) {
        Tokens existingUserToken = tokenRepo.findByReference(email);

        if(existingUserToken != null) tokenRepo.delete(existingUserToken);

        var token = getToken();
        long currentTimeMillis = System.currentTimeMillis();
        long expiration =  currentTimeMillis + (24L * 60L * 60L * 1000L);
        Tokens tokens = new Tokens();
        tokens.setToken(token);
        tokens.setReference(email);
        tokens.setCreatedOn(currentTimeMillis);
        tokens.setExpiration(expiration);

        tokenRepo.save(tokens);

        return token;
    }

    @Scheduled(fixedDelay = 30000)
    public void deleteToken(String reference, String token){
        Tokens tokens = tokenRepo.findByReferenceAndToken(reference,token).get();

        if(token != null) tokenRepo.delete(tokens);
    }

    @Override
    public Boolean validateToken(String email,String token){
        Tokens tokens = tokenRepo.findByReferenceAndToken(email,token).orElseThrow(() -> new IllegalArgumentException("Token Not Recognized"));

        long expiry = tokens.getExpiration();
        long currentTime = System.currentTimeMillis();
        deleteToken(email, token);
        return currentTime > expiry;
    }

    private String getToken(){
        Random random = new Random();
        var digit = 100_000 + random.nextInt(900_000);
        return String.valueOf(digit);
    }


}
