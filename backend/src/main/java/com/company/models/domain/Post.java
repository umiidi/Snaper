package com.company.models.domain;

import com.company.models.domain.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    int id;
    User user;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    int views;

}
