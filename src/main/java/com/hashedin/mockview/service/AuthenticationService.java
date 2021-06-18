package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.UserInputRequest;
import com.hashedin.mockview.exception.InvalidPasswordException;
import com.hashedin.mockview.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    JwtUtil jwtUtil;

    public String createAuthenticationToken(UserInputRequest userInputRequest) throws InvalidPasswordException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userInputRequest.getEmailId(),
                            userInputRequest.getPassword()));
        } catch (BadCredentialsException exception) {
            throw new InvalidPasswordException("Incorrect Username or Password");
        }
        return generateJwtTokenForCurrentUser(userInputRequest.getEmailId());

    }

    private String generateJwtTokenForCurrentUser(String emailId) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(emailId);
        return jwtUtil.generateToken(userDetails);
    }
}
