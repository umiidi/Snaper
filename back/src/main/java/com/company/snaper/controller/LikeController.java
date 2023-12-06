package com.company.snaper.controller;

import com.company.snaper.models.base.BaseResponse;
import com.company.snaper.services.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{post-id}")
    public BaseResponse<?> get(int postId) {
        return BaseResponse.success(likeService.allLikes(postId));
    }

    @PostMapping("{post-id}")
    public BaseResponse<?> like(int postId) {
        likeService.like(postId);
        return BaseResponse.success();
    }

    @DeleteMapping("/{post-id}")
    public BaseResponse<?> unlike(int postId) {
        likeService.unlike(postId);
        return BaseResponse.success();
    }

}
