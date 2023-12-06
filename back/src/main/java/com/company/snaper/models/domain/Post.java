package com.company.snaper.models.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {

    int id;
    User user;
    String content;
    LocalDate createdAt;
    LocalDate updatedAt;
    int views;

}
