package com.swiftdelivery.authenticator.model.user;

import com.swiftdelivery.authenticator.auth.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UsersRepository usersRepository;

    public  UsersEntity extractUser(
            HttpServletRequest servletRequest
    ){
        String authHeader = servletRequest.getHeader("Authorization");
        String jwt  =  authHeader.substring(7);
        String email = jwtUtils.extractUsername(jwt);
        UsersEntity user = usersRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return  user;
    }

}
