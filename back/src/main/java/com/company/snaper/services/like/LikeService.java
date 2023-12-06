package com.company.snaper.services.like;

import com.company.snaper.models.response.LikeResponse;

import java.util.List;

public interface LikeService {

    List<LikeResponse> allLikes(int postId);

    void like(int postId);

    void unlike(int postId);

    void deletePostLikes(int postId);

}
