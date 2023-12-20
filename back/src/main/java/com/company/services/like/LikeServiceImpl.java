package com.company.services.like;

import com.company.exception.types.NotFoundException;
import com.company.models.domain.User;
import com.company.models.response.LikeResponse;
import com.company.repository.LikeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepo likeRepo;

    @Override
    public List<LikeResponse> allLikes(int postId) {
        var likes = likeRepo.getAllLikes(postId).orElseThrow(
                () -> new NotFoundException("post not found")
        );
        return likes.stream().map(like -> LikeResponse.builder()
                        .username(like.getUser().getUsername())
                        .imageUrl(like.getUser().getImgUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public int countLikes(int postId) {
        return likeRepo.countLikes(postId);
    }

    @Override
    public boolean isLiked(int postId) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return likeRepo.isLike(postId, auth.getId());
    }

    @Override
    public void like(int postId) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!likeRepo.isLike(postId, auth.getId())) {
            likeRepo.like(postId, auth.getId());
        }
    }

    @Override
    public void unlike(int postId) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        likeRepo.unlike(postId, auth.getId());
    }

    @Override
    public void deletePostLikes(int postId) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        likeRepo.deleteAllLikeByPostId(postId);
    }

}
