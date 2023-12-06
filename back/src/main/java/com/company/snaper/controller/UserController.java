package com.company.snaper.controller;

import com.company.snaper.models.base.BaseResponse;
import com.company.snaper.models.request.UserRequest;
import com.company.snaper.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public BaseResponse<?> get(int id) {
        return BaseResponse.success(userService.get(id));
    }

    @GetMapping("/{username}")
    public BaseResponse<?> get(String username) {
        return BaseResponse.success(userService.get(username));
    }

    @GetMapping
    public BaseResponse<?> get() {
        return BaseResponse.success(userService.get());
    }

    @PutMapping()
    public BaseResponse<?> update(UserRequest userRequest) {
        userService.update(userRequest);
        return BaseResponse.success();
    }

    @DeleteMapping()
    public BaseResponse<?> deactive() {
        userService.deactive();
        return BaseResponse.success();
    }

    @PutMapping("/active/{id}")
    public BaseResponse<?> active(int id) {
        userService.active(id);
        return BaseResponse.success();
    }

}
