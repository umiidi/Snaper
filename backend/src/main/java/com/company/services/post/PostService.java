package com.company.services.post;

import com.company.models.domain.Post;
import com.company.models.request.PostRequest;
import com.company.models.response.PostResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PostService {

    List<PostResponse> getAll();

    PostResponse get(int postId);

    void add(PostRequest postRequest);

    void update(int postId, PostRequest postRequest) throws AccessDeniedException;

    void delete(int postId) throws AccessDeniedException;


}
