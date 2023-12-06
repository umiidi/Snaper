package com.company.snaper.controller;

import com.company.snaper.models.base.BaseResponse;
import com.company.snaper.models.dto.AccessTokenDto;
import com.company.snaper.models.dto.LoginDto;
import com.company.snaper.models.dto.SignUpAdminDto;
import com.company.snaper.models.dto.SignUpUserDto;
import com.company.snaper.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public BaseResponse<AccessTokenDto> authorize(@RequestBody LoginDto authDto) {
        return BaseResponse.success(authService.signIn(authDto));
    }

    @PostMapping("/sign-up/")
    public BaseResponse<?> signUp(@RequestBody SignUpUserDto signUpUserDto) {
        authService.signUp(signUpUserDto);
        return BaseResponse.success();
    }

    @PostMapping("/sign-up/admin")
    public BaseResponse<?> signUp(@RequestBody SignUpAdminDto signUpAdminDto) {
        authService.signUp(signUpAdminDto);
        return BaseResponse.success();
    }

}
