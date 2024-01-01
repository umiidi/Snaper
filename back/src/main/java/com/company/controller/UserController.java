package com.company.controller;

import com.company.models.base.BaseResponse;
import com.company.models.request.UserRequest;
import com.company.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public BaseResponse<?> get(@PathVariable("id") int id) {
        return BaseResponse.success(userService.get(id));
    }

    @GetMapping("/{username}")
    public BaseResponse<?> get(@PathVariable("username") String username) {
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
    public BaseResponse<?> active(@PathVariable("id") int id) {
        userService.active(id);
        return BaseResponse.success();
    }

}
