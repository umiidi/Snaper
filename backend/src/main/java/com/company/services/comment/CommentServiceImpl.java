package com.company.services.comment;

import com.company.models.domain.Comment;
import com.company.models.domain.User;
import com.company.models.enums.Authority;
import com.company.models.request.CommentRequest;
import com.company.models.response.CommentResponse;
import com.company.repository.CommentRepo;
import io.swagger.v3.oas.models.links.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;

    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .name(comment.getUser().getName())
                .username(comment.getUser().getUsername())
                .imageUrl(comment.getUser().getImgUrl())
                .id(comment.getId())
                .content(comment.getContent())
                .access(isAllowed(comment.getUser().getId()))
                .commentedAt(comment.getCommentedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    private boolean isAllowed(int userId) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return auth.getAuthority() == Authority.ADMIN || auth.getId() == userId;
    }

    @Override
    public List<List<CommentResponse>> getAll(int postId) {
        List<Comment> comments = commentRepo.findAllByPostId(postId).orElseThrow(
                () -> new IllegalArgumentException("post not found")
        );
        List<List<CommentResponse>> all = new LinkedList<>();
        for (var comment : comments) {
            List<CommentResponse> subComments = new LinkedList<>();
//            commentRepo.findAllByParentId(comment.getPostId(), comment.getId())
//                    .stream().map(CommentServiceImpl::toCommentResponse).forEach(subComments::add);
            subComments.add(0, toCommentResponse(comment));
            all.add(subComments);
        }
        return all;
    }

    @Override
    public int countComments(int postId) {
        return commentRepo.countComments(postId);
    }

    @Override
    public void add(int postId, CommentRequest commentRequest) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentRepo.save(Comment.builder()
                .user(User.builder().id(auth.getId()).build())
                .postId(postId)
                .content(commentRequest.getContent())
                .commentedAt(LocalDateTime.now())
                .parentCommentId(commentRequest.getParentCommentId())
                .build());
    }

    @Override
    public void update(int id, CommentRequest commentRequest) throws AccessDeniedException {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth.getId() == commentRepo.getUserId(id)) {
            commentRepo.update(Comment.builder()
                    .id(id)
                    .content(commentRequest.getContent())
                    .updatedAt(LocalDateTime.now())
                    .build());
        } else {
            throw new AccessDeniedException("can't update a comment");
        }
    }

    @Override
    public void delete(int commentId) throws AccessDeniedException {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (auth.getAuthority() == Authority.ADMIN || auth.getId() == commentRepo.getUserId(commentId)) {
            commentRepo.deleteByParentId(commentId);
            commentRepo.delete(commentId);
        } else {
            throw new AccessDeniedException("can't delete a comment");
        }
    }

    @Override
    public void deletePostComments(int postId) {
        commentRepo.deleteByPostId(postId);
    }

}
