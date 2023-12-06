package com.company.snaper.models.response;

import com.company.snaper.models.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String username;
    String name;
    String surname;
    String imgUrl;
    String email;
    String phone;
    Gender gender;

}
