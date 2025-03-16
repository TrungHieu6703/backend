package com.example.backend.entity;

import com.example.backend.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String username;

    String password;

    String name;

    @Enumerated(EnumType.STRING)
    RoleEnum role;

    String phone;

    String email;

    @OneToMany(mappedBy = "user")
    Set<Order> orders = new HashSet<>();
}
