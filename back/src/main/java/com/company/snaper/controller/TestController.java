package com.company.snaper.controller;

import com.company.snaper.models.base.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping()
    public String get() {
        return "sagol";
    }

    @GetMapping("/{id}")
    public BaseResponse<?> get(@PathVariable String id) {
        return BaseResponse.success("salam!" + id);
    }

}
