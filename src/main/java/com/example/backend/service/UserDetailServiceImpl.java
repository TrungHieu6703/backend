package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));
        String role = String.valueOf(user.getRole());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getName())
                .password(user.getPassword())
                .authorities(role)
                .build();
    }
}
