package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.repository.HomeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HomeService {

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Object getHomePageData() {
        try {
            String jsonData = homeRepository.getHomePageData();
            // Chuyển đổi chuỗi JSON thành đối tượng JsonNode để trả về
            return objectMapper.readTree(jsonData);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy dữ liệu trang chủ", e);
        }
    }
}