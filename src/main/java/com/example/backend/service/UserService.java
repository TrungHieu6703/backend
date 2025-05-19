package com.example.backend.service;

import com.example.backend.dto.request.ChangePasswordDTO;
import com.example.backend.dto.request.EmailDTO;
import com.example.backend.dto.request.LoginDTO;
import com.example.backend.dto.request.UserDTO;
import com.example.backend.dto.response.UserRes;
import com.example.backend.entity.User;
import com.example.backend.enums.RoleEnum;
import com.example.backend.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
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

    @Autowired
    private MailService mailService;

    public UserRes createUser(UserDTO userDTO) {
        User user = new User();
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
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());

        User updatedUser = userRepo.save(user);

        return new UserRes(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getPassword(), updatedUser.getName(),updatedUser.getRole(),updatedUser.getPhone(), updatedUser.getEmail());
    }

    public UserRes changeUserPassword(String id, ChangePasswordDTO changePasswordDTO) {
        // Kiểm tra xem mật khẩu mới và xác nhận mật khẩu có khớp nhau không
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        // Tìm người dùng theo ID
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Xác thực mật khẩu hiện tại
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        User updatedUser = userRepo.save(user);

        return new UserRes(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getPassword(),
                updatedUser.getName(),
                updatedUser.getRole(),
                updatedUser.getPhone(),
                updatedUser.getEmail()
        );
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

    public UserRes getUserByEmail(String email) {
        System.out.println(email);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserRes(
                user.getId(),
                user.getUsername(),
                null, // Không trả về password
                user.getName(),
                user.getRole(),
                user.getPhone(),
                user.getEmail()
        );
    }

    public ResponseEntity<?> sendMail(EmailDTO emailDTO) {
        try {
            Boolean hasEmail = userRepo.existsByEmail(emailDTO.getEmail());
            if (!hasEmail) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("email not found");
            }
            User user = userRepo.findByEmail(emailDTO.getEmail()).orElseThrow(
                    () -> new EntityNotFoundException("Can't find email")
            );
            String randomPassword = UserService.randomString(15);
            user.setPassword(passwordEncoder.encode(randomPassword));
            userRepo.save(user);
            mailService.sendEmail(emailDTO.getEmail(), randomPassword);
        } catch (MailException mailException) {
            System.out.println(mailException);
        }

        return ResponseEntity.ok("Congratulations! Your mail has been sent to the user.");
    }

    public static String randomString(int length) {
         String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
         SecureRandom RANDOM = new SecureRandom();
        return RANDOM.ints(length, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
