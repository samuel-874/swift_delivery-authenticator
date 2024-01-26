package com.swiftdelivery.authenticator.model.user;

import com.swiftdelivery.authenticator.response.UserRegistrationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UsersService {

    ResponseEntity registerUser(UserRegistrationDTO userDTO);


    ResponseEntity sendVerificationMail(String email);

    ResponseEntity reSendVerificationMail(String email);

    ResponseEntity validateVerificationMail(String email, String token);

    ResponseEntity login(UsersServiceImpl.AuthenticationRequest request);

    ResponseEntity getUserDetails();
}
