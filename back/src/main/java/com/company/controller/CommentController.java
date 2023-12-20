package com.company.controller;

import com.company.models.base.BaseResponse;
import com.company.models.request.CommentRequest;
import com.company.services.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{post-id}")
    public BaseResponse<?> get(@PathVariable("post-id") int postId) {
        return BaseResponse.success(commentService.getAll(postId));
    }

    @PostMapping("/{post-id}")
    public BaseResponse<?> create(@PathVariable("post-id") int postId, @RequestBody CommentRequest comment) {
        commentService.add(postId, comment);
        return BaseResponse.success();
    }

    @PutMapping("/{id}")
    public BaseResponse<?> update(@PathVariable("id") int id, @RequestBody CommentRequest comment) throws AccessDeniedException {
        commentService.update(id, comment);
        return BaseResponse.success();
    }

    @DeleteMapping("/{id}")
    public BaseResponse<?> delete(@PathVariable("id") int id) throws AccessDeniedException {
        commentService.delete(id);
        return BaseResponse.success();
    }

}
