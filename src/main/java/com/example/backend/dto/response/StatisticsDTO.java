package com.example.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDTO {
    // Thông tin tổng quan
    private int totalOrders;
    private BigDecimal totalRevenue;
    private int pendingOrders;
    private int rejectedOrders;
    private int totalProducts;

    // Thông tin chi tiết
    private Object topProducts;

    // Thông tin doanh thu theo khoảng thời gian
    private Map<String, BigDecimal> revenueByDate;

    // Thông tin đơn hàng theo khoảng thời gian
    private Map<String, Integer> ordersByDate;

    // Thông tin doanh thu theo trạng thái
    private Map<String, BigDecimal> revenueByStatus;

    // Thông tin đơn hàng theo người dùng
    private Map<String, Integer> ordersByUser;

    // Thông tin sản phẩm tồn kho thấp
    private List<Map<String, Object>> lowStockProducts;
}