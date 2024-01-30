package com.company.models.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {

    String name;
    String surname;
    String username;
    String imageUrl;

    int id;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isLiked;
    boolean access;
    int numberOfLikes;
    int numberOfComments;
    int views;

}
