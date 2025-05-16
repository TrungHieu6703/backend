package com.example.backend.controller;

import com.example.backend.dto.response.StatisticsDTO;
import com.example.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // Lấy thống kê tổng quan
    @GetMapping("/dashboard")
    public ResponseEntity<StatisticsDTO> getDashboardStatistics() {
        StatisticsDTO statistics = statisticsService.getDashboardStatistics();
        return ResponseEntity.ok(statistics);
    }

    // Lấy thống kê theo khoảng thời gian
    @GetMapping("/time-range")
    public ResponseEntity<Map<String, Object>> getTimeRangeStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<String, Object> statistics = statisticsService.getTimeRangeStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }

    // Lấy sản phẩm bán chạy
    @GetMapping("/top-products")
    public ResponseEntity<Map<String, Object>> getTopProducts(
            @RequestParam(defaultValue = "10") int limit) {

        Map<String, Object> topProducts = statisticsService.getTopProducts(limit);
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/pending-orders-count")
    public ResponseEntity<Map<String, Integer>> getPendingOrdersCount() {
        int pendingOrders = statisticsService.getPendingOrdersCount();
        Map<String, Integer> response = Map.of("pendingOrders", pendingOrders);
        return ResponseEntity.ok(response);
    }
}