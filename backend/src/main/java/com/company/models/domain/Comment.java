package com.company.models.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    int id;
    User user;
    int postId;
    String content;
    int parentCommentId;
    LocalDateTime commentedAt;
    LocalDateTime updatedAt;

}
