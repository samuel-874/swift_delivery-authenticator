package com.swiftdelivery.authenticator.response;


import com.swiftdelivery.authenticator.model.user.UserProvider;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private UserProvider provider;
}
