package com.company.auth;

import com.company.models.domain.User;
import com.company.services.user.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        User account = (User) userDetailsService.loadUserByUsername(username);
        String hashPassword = account.getPassword();
        String password = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(password,hashPassword)) throw new BadCredentialsException("Username or password is invalid");
        return new UsernamePasswordAuthenticationToken(account.getUsername(),account.getPassword(),account.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication){
        return false;
    }
}
