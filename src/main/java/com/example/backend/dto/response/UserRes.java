package com.example.backend.dto.response;

import com.example.backend.enums.RoleEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserRes {
    String id;

    String username;

    String password;

    String name;

    RoleEnum role;

    String phone;

    String email;
}
