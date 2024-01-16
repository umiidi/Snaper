package com.company.models.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {

    String name;
    String username;
    String imageUrl;

    int id;
    String content;
    boolean access;
    LocalDateTime commentedAt;
    LocalDateTime updatedAt;

}
