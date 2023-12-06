package com.company.snaper.services.user;

import com.company.snaper.models.domain.User;
import com.company.snaper.models.dto.SignUpAdminDto;
import com.company.snaper.models.dto.SignUpUserDto;
import com.company.snaper.models.request.UserRequest;
import com.company.snaper.models.response.UserResponse;

public interface UserService {

    UserResponse get(int id);

    UserResponse get(String username);

    UserResponse get();

    User getByUsername(String username);

    void add(SignUpUserDto signUpUserDto);

    void add(SignUpAdminDto signUpAdminDto);

    void update(UserRequest userRequest);

    void deactive();

    void active(int id);

}
