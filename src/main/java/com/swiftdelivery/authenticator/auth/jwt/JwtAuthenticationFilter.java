package com.swiftdelivery.authenticator.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftdelivery.authenticator.model.user.CustomUserDetailsService;
import com.swiftdelivery.authenticator.model.user.UsersRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    private UsersRepository userRepository;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String emailOrPhoneNumber;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            emailOrPhoneNumber = jwtUtils.extractUsername(jwt);

            if (emailOrPhoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.loadUserByUsername(emailOrPhoneNumber);
                if (jwtUtils.isTokenValid(jwt, userDetails)) {
                     UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                          userDetails,
                          null,
                           userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    sendError(response, "Token expired");
                }
            } else {
                sendError(response, "Invalid token. No sub attached");
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex){
            sendError(response,"Jwt Expired");
        }

        catch (Exception ex) {
            sendError(response,"Unable to process request");
        }
    }

    private void sendError(HttpServletResponse response,
                           String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), Map.of("error", errorMessage));
    }
}