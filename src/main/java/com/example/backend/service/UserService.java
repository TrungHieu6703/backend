package com.example.backend.service;

import com.example.backend.dto.request.UserDTO;
import com.example.backend.dto.response.UserRes;
import com.example.backend.entity.User;
import com.example.backend.enums.RoleEnum;
import com.example.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public UserRes createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setRole(RoleEnum.USER);
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());

        User result = userRepo.save(user);

        return new UserRes(
                result.getId(),
                result.getUsername(),
                result.getPassword(),
                result.getName(),
                result.getRole(),
                result.getPhone(),
                result.getEmail());
    }

    public UserRes updateUser(String id, UserDTO userDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());

        User updatedUser = userRepo.save(user);

        return new UserRes(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getPassword(), updatedUser.getName(),updatedUser.getRole(),updatedUser.getPhone(), updatedUser.getEmail());
    }

    public void deleteUser(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(user);
    }

    public UserRes getUserById(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserRes(user.getId(), user.getUsername(), user.getPassword(), user.getName(),user.getRole(),user.getPhone(), user.getEmail());
    }

    public List<UserRes> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(user -> new UserRes(user.getId(), user.getUsername(), user.getPassword(), user.getName(),user.getRole(),user.getPhone(), user.getEmail()))
                .collect(Collectors.toList());
    }
}
