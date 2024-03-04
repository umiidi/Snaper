package com.company.services.post;

import com.company.exception.types.NotFoundException;
import com.company.models.domain.Post;
import com.company.models.domain.User;
import com.company.models.enums.Authority;
import com.company.models.request.PostRequest;
import com.company.models.response.PostResponse;
import com.company.repository.PostRepo;
import com.company.services.comment.CommentService;
import com.company.services.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final CommentService commentService;
    private final LikeService likeService;

    @Override
    public List<PostResponse> getAll() {
        postRepo.views();
        return postRepo.findAll().stream().map(post -> PostResponse.builder()
                        .name(post.getUser().getName())
                        .surname(post.getUser().getSurname())
                        .username(post.getUser().getUsername())
                        .imageUrl(post.getUser().getImgUrl())
                        .id(post.getId())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .isLiked(likeService.isLiked(post.getId()))
                        .views(post.getViews())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PostResponse get(int postId) {
        postRepo.view(postId);
        var post = postRepo.find(postId).orElseThrow(
                () -> new NotFoundException("post not found")
        );
        return PostResponse.builder()
                .name(post.getUser().getName())
                .surname(post.getUser().getSurname())
                .username(post.getUser().getUsername())
                .imageUrl(post.getUser().getImgUrl())
                .id(post.getId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .isLiked(likeService.isLiked(post.getId()))
                .access(isAllowed(post.getUser().getId()))
                .numberOfLikes(likeService.countLikes(post.getId()))
                .numberOfComments(commentService.countComments(post.getId()))
                .views(post.getViews())
                .build();
    }

    @Override
    public void add(PostRequest postRequest) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        postRepo.save(Post.builder()
                .user(User.builder()
                        .id(auth.getId())
                        .build())
                .content(postRequest.getContent())
                .createdAt(LocalDateTime.now())
                .views(0)
                .build());
    }

    @Override
    public void update(int postId, PostRequest postRequest) throws AccessDeniedException {
        if (isAllowed(postId)) {
            postRepo.update(Post.builder()
                    .id(postId)
                    .content(postRequest.getContent())
                    .updatedAt(LocalDateTime.now())
                    .build());
        } else {
            throw new AccessDeniedException("can't update a post");
        }
    }

    @Override
    public void delete(int postId) throws AccessDeniedException {
        if (isAllowed(postId)) {
            likeService.deletePostLikes(postId);
            commentService.deletePostComments(postId);
            postRepo.delete(postId);
        } else {
            throw new AccessDeniedException("can't delete a post");
        }
    }

    private boolean isAllowed(int userId) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return auth.getAuthority() == Authority.ADMIN || auth.getId() == postRepo.getUserId(postId);
    }

}
