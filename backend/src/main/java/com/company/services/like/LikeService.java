package com.company.services.like;

import com.company.models.response.LikeResponse;

import java.util.List;

public interface LikeService {

    List<LikeResponse> allLikes(int postId);

    int countLikes(int postId);

    boolean isLiked(int postId);

    void like(int postId);

    void unlike(int postId);

    void deletePostLikes(int postId);

}
