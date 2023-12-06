package com.company.snaper.models.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    LocalDate commentedAt;
    LocalDate updatedAt;

}
