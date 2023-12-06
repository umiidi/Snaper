package com.company.snaper.controller;

import com.company.snaper.models.base.BaseResponse;
import com.company.snaper.models.request.CommentRequest;
import com.company.snaper.services.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{post-id}")
    public BaseResponse<?> get(int postId) {
        return BaseResponse.success(commentService.getAll(postId));
    }

    @PostMapping()
    public BaseResponse<?> create(int postId, @RequestBody CommentRequest comment) {
        commentService.add(postId, comment);
        return BaseResponse.success();
    }

    @PutMapping("/{id}")
    public BaseResponse<?> update(int id, @RequestBody CommentRequest comment) throws AccessDeniedException {
        commentService.update(id, comment);
        return BaseResponse.success();
    }

    @DeleteMapping("/{id}")
    public BaseResponse<?> delete(int commentId) throws AccessDeniedException {
        commentService.delete(commentId);
        return BaseResponse.success();
    }

}
