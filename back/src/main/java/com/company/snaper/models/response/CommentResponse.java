package com.company.snaper.models.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {

    String username;
    String imageUrl;

    String content;
    LocalDate commentedAt;
    LocalDate updatedAt;

}
