package com.example.backend.service;

import com.example.backend.dto.request.LoginDTO;
import com.example.backend.dto.request.UserDTO;
import com.example.backend.dto.response.UserRes;
import com.example.backend.entity.User;
import com.example.backend.enums.RoleEnum;
import com.example.backend.repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserRes createUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setRole(RoleEnum.ROLE_USER);
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

    public Map<String, String> login(LoginDTO loginDTO, HttpServletResponse rs, HttpServletRequest req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
            );

            String token = jwtService.generateToken(loginDTO.email());
            String refreshToken = jwtService.generateRefreshToken(loginDTO.email());

            // Trả về JSON hợp lệ
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
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
