package com.swiftdelivery.authenticator.mappers;

import com.swiftdelivery.authenticator.model.user.UsersEntity;
import com.swiftdelivery.authenticator.response.UserRegistrationDTO;

public class Mapper {

    public static UsersEntity mapUserEntity(UserRegistrationDTO registrationDTO){

        return UsersEntity.builder()
                .firstname(registrationDTO.getFirstname())
                .lastname(registrationDTO.getLastname())
                .email(registrationDTO.getEmail())
                .provider(registrationDTO.getProvider())
                .build();
    }
}
