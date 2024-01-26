package com.swiftdelivery.authenticator.model.user;

import com.swiftdelivery.authenticator.model.user.enums.AccStatus;
import com.swiftdelivery.authenticator.model.user.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersEntity {

    @Id
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "users_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "users_seq")
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private UserProvider provider;
    private String APIKey;
    private Roles userRole;
    private long lastLogin;
    private AccStatus accStatus;

}
