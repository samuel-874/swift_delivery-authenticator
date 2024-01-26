package com.swiftdelivery.authenticator.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email:"+ username));

            return new User(users.getEmail(),users.getPassword(),
                    Stream.of(users.getUserRole())
                            .map(role -> new SimpleGrantedAuthority(users.getUserRole().toString()))
                            .collect(Collectors.toList()));
    }
}
