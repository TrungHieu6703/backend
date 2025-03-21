package com.example.backend.controller;

import com.example.backend.dto.request.UserDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.UserRes;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Tạo mới User
    @PostMapping
    public ResponseEntity<ApiResponse<UserRes>> createUser(@RequestBody UserDTO userDTO) {
        UserRes userRes = userService.createUser(userDTO);
        ApiResponse<UserRes> response = new ApiResponse<>("User created successfully", HttpStatus.CREATED.value(), userRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật User theo ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRes>> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserRes updatedUser = userService.updateUser(id, userDTO);
        ApiResponse<UserRes> response = new ApiResponse<>("User updated successfully", HttpStatus.OK.value(), updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa User theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = new ApiResponse<>("User deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy User theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRes>> getUserById(@PathVariable String id) {
        UserRes userRes = userService.getUserById(id);
        ApiResponse<UserRes> response = new ApiResponse<>("User retrieved successfully", HttpStatus.OK.value(), userRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả User
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserRes>>> getAllUsers() {
        List<UserRes> users = userService.getAllUsers();
        ApiResponse<List<UserRes>> response = new ApiResponse<>("All users retrieved successfully", HttpStatus.OK.value(), users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
