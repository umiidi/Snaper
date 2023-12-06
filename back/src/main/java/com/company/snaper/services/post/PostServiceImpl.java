package com.company.snaper.services.post;

import com.company.snaper.exception.types.NotFoundException;
import com.company.snaper.models.domain.Post;
import com.company.snaper.models.domain.User;
import com.company.snaper.models.enums.Authority;
import com.company.snaper.models.request.PostRequest;
import com.company.snaper.models.response.LikeResponse;
import com.company.snaper.models.response.PostResponse;
import com.company.snaper.repository.PostRepo;
import com.company.snaper.services.comment.CommentService;
import com.company.snaper.services.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
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
        return postRepo.findAll().stream().map(post -> PostResponse.builder()
                        .username(post.getUser().getUsername())
                        .imageUrl(post.getUser().getImgUrl())
                        .content(post.getContent())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
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
                .username(post.getUser().getUsername())
                .imageUrl(post.getUser().getImgUrl())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
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
                .createdAt(LocalDate.now())
                .views(0)
                .build());
    }

    @Override
    public void update(int postId, PostRequest postRequest) throws AccessDeniedException {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth.getAuthority() == Authority.ADMIN || auth.getId() == postRepo.getUserId(postId)) {
            postRepo.update(Post.builder()
                    .id(postId)
                    .content(postRequest.getContent())
                    .updatedAt(LocalDate.now())
                    .build());
        } else {
            throw new AccessDeniedException("can't update a post");
        }
    }

    @Override
    public void delete(int postId) throws AccessDeniedException {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth.getAuthority() == Authority.ADMIN || auth.getId() == postRepo.getUserId(postId)) {
            likeService.deletePostLikes(postId);
            commentService.deletePostComments(postId);
            postRepo.delete(postId);
        } else {
            throw new AccessDeniedException("can't delete a post");
        }
    }

}
