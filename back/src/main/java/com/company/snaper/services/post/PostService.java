package com.company.snaper.services.post;

import com.company.snaper.models.domain.Post;
import com.company.snaper.models.request.PostRequest;
import com.company.snaper.models.response.PostResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PostService {

    List<PostResponse> getAll();

    PostResponse get(int postId);

    void add(PostRequest postRequest);

    void update(int postId, PostRequest postRequest) throws AccessDeniedException;

    void delete(int postId) throws AccessDeniedException;


}
