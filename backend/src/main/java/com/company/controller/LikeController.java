package com.company.controller;

import com.company.models.base.BaseResponse;
import com.company.services.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{post-id}")
    public BaseResponse<?> get(@PathVariable("post-id") int postId) {
        return BaseResponse.success(likeService.allLikes(postId));
    }

    @PostMapping("/{post-id}")
    public BaseResponse<?> like(@PathVariable("post-id") int postId) {
        likeService.like(postId);
        return BaseResponse.success();
    }

    @DeleteMapping("/{post-id}")
    public BaseResponse<?> unlike(@PathVariable("post-id") int postId) {
        likeService.unlike(postId);
        return BaseResponse.success();
    }

}
