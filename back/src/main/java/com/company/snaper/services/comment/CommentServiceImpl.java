package com.company.snaper.services.comment;

import com.company.snaper.models.domain.Comment;
import com.company.snaper.models.domain.User;
import com.company.snaper.models.enums.Authority;
import com.company.snaper.models.request.CommentRequest;
import com.company.snaper.models.response.CommentResponse;
import com.company.snaper.repository.CommentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepo commentRepo;

    private static CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .username(comment.getUser().getUsername())
                .imageUrl(comment.getUser().getImgUrl())
                .content(comment.getContent())
                .commentedAt(comment.getCommentedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    @Override
    public List<List<CommentResponse>> getAll(int postId) {
        List<Comment> comments = commentRepo.findAllByPostId(postId).orElseThrow(
                () -> new IllegalArgumentException("post not found")
        );
        List<List<CommentResponse>> all = new ArrayList<>(comments.size());
        for (var comment : comments) {
            List<CommentResponse> subComments = commentRepo.findAllByParentId(comment.getPostId(), comment.getId())
                    .stream().map(CommentServiceImpl::toCommentResponse).collect(Collectors.toList());
            subComments.add(0, toCommentResponse(comment));
            all.add(subComments);
        }
        return all;
    }

    @Override
    public void add(int postId, CommentRequest commentRequest) {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentRepo.save(Comment.builder()
                .user(User.builder().id(auth.getId()).build())
                .postId(postId)
                .content(commentRequest.getContent())
                .commentedAt(LocalDate.now())
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
                    .updatedAt(LocalDate.now())
                    .build());
        }else {
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
