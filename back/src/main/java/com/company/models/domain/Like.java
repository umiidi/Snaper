package com.company.models.domain;

import com.company.models.domain.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Like {

    int id;
    User user;
    int postId;
    
}
