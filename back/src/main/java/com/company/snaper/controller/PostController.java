package com.company.snaper.controller;

import com.company.snaper.models.base.BaseResponse;
import com.company.snaper.models.request.PostRequest;
import com.company.snaper.services.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public BaseResponse<?> get(@PathVariable int id) {
        return BaseResponse.success(postService.get(id));
    }

    @GetMapping
    public BaseResponse<?> get() {
        return BaseResponse.success(postService.getAll());
    }

    @PostMapping
    public BaseResponse<?> create(PostRequest postRequest) {
        postService.add(postRequest);
        return BaseResponse.success();
    }

    @PutMapping("/{id}")
    public BaseResponse<?> update(int id, @RequestBody PostRequest postRequest) throws AccessDeniedException {
        postService.update(id, postRequest);
        return BaseResponse.success();
    }

    @DeleteMapping("/{id}")
    public BaseResponse<?> delete(int id) throws AccessDeniedException {
        postService.delete(id);
        return BaseResponse.success();
    }

}
