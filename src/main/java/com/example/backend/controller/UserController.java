package com.example.backend.controller;

import com.example.backend.dto.request.ChangePasswordDTO;
import com.example.backend.dto.request.EmailDTO;
import com.example.backend.dto.request.LoginDTO;
import com.example.backend.dto.request.UserDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.UserRes;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse rs, HttpServletRequest req) {
        Map<String, String> response = userService.login(loginDTO, rs, req);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserRes>> getCurrentUser(HttpServletRequest request) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserRes currentUser = userService.getUserByEmail(email);
        ApiResponse<UserRes> response = new ApiResponse<>("User information retrieved successfully", HttpStatus.OK.value(), currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // Tạo mới User
    @PostMapping("/register")
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

    @PutMapping("change-password/{id}")
    public ResponseEntity<ApiResponse<UserRes>> changePassword(@PathVariable String id, @RequestBody ChangePasswordDTO changePasswordDTO) {
        UserRes updatedUser = userService.changeUserPassword(id, changePasswordDTO);
        ApiResponse<UserRes> response = new ApiResponse<>("Password updated successfully", HttpStatus.OK.value(), updatedUser);
        return ResponseEntity.ok(response);
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

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendMail(@RequestBody EmailDTO emailDTO) {
        return userService.sendMail(emailDTO);
    }
}
