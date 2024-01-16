package com.company.services.user;

import com.company.models.domain.User;
import com.company.models.dto.SignUpAdminDto;
import com.company.models.dto.SignUpUserDto;
import com.company.models.request.UserRequest;
import com.company.models.response.UserResponse;

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
