package com.company.snaper.services.auth;

import com.company.snaper.models.dto.AccessTokenDto;
import com.company.snaper.models.dto.LoginDto;
import com.company.snaper.models.dto.SignUpAdminDto;
import com.company.snaper.models.dto.SignUpUserDto;

public interface AuthService {

    AccessTokenDto signIn(LoginDto signInDto);

    void signUp(SignUpUserDto signUpDto);

    void signUp(SignUpAdminDto signUpDto);


}
