package com.swiftdelivery.authenticator.model.tokens;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tokens {
    @Id
    @SequenceGenerator(
            name = "token_seq",
            sequenceName = "token_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "token_seq")
    private long id;
    private String token;
    private long createdOn;
    private long expiration;
    private String reference;
}
