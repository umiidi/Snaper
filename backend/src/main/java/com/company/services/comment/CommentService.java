package com.company.services.comment;

import com.company.models.request.CommentRequest;
import com.company.models.response.CommentResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CommentService {

    List<List<CommentResponse>> getAll(int postId);

    int countComments(int postId);

    void add(int postId, CommentRequest commentRequest);

    void update(int id, CommentRequest commentRequest) throws AccessDeniedException;

    void delete(int commentId) throws AccessDeniedException;

    void deletePostComments(int postId);

}
