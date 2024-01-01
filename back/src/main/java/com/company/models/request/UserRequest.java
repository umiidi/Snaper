package com.company.models.request;

import com.company.models.enums.Gender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

    String username;
    String password;

    String name;
    String surname;
    String imgUrl;
    String email;
    String phone;
    Gender gender;

}
