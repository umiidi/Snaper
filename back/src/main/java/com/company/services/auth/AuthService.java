package com.company.services.auth;

import com.company.models.dto.AccessTokenDto;
import com.company.models.dto.LoginDto;
import com.company.models.dto.SignUpAdminDto;
import com.company.models.dto.SignUpUserDto;

public interface AuthService {

    AccessTokenDto signIn(LoginDto signInDto);

    void signUp(SignUpUserDto signUpDto);

    void signUp(SignUpAdminDto signUpDto);


}
