package com.company.services.auth;

import com.company.models.dto.AccessTokenDto;
import com.company.models.dto.LoginDto;
import com.company.models.dto.SignUpAdminDto;
import com.company.models.dto.SignUpUserDto;
import com.company.services.user.UserService;
import com.company.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AccessTokenDto signIn(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateJwtToken(authentication);
        return new AccessTokenDto(token);
    }

    @Override
    public void signUp(SignUpUserDto signUpDto) {
        userService.add(signUpDto);
    }

    @Override
    public void signUp(SignUpAdminDto signUpDto) {
        userService.add(signUpDto);
    }

}
