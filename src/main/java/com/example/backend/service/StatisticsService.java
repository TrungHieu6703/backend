package com.example.backend.service;

import com.example.backend.dto.response.StatisticsDTO;
import com.example.backend.entity.Order;
import com.example.backend.entity.OrderDetail;
import com.example.backend.entity.Product;
import com.example.backend.enums.StatusEnum;
import com.example.backend.repository.OrderDetailRepo;
import com.example.backend.repository.OrderRepo;
import com.example.backend.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @Autowired
    private ProductRepo productRepo;

    // Thống kê tổng quan
    public StatisticsDTO getDashboardStatistics() {
        StatisticsDTO statistics = new StatisticsDTO();

        // Tổng số đơn hàng
        List<Order> allOrders = orderRepo.findAll();
        statistics.setTotalOrders(allOrders.size());

        // Tổng doanh thu từ đơn hàng đã hoàn thành
        BigDecimal totalRevenue = allOrders.stream()
                .filter(order -> order.getStatus() == StatusEnum.APPROVED)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        statistics.setTotalRevenue(totalRevenue);

        // Số lượng đơn chờ xử lý
        long pendingOrders = allOrders.stream()
                .filter(order -> order.getStatus() == StatusEnum.PENDING)
                .count();
        statistics.setPendingOrders((int) pendingOrders);

        // Số lượng đơn đã hủy
        long rejectedOrders = allOrders.stream()
                .filter(order -> order.getStatus() == StatusEnum.REJECTED)
                .count();
        statistics.setRejectedOrders((int) rejectedOrders);

        // Tổng số sản phẩm
        long totalProducts = productRepo.count();
        statistics.setTotalProducts((int) totalProducts);

        // Sản phẩm bán chạy
        Map<String, Object> topProducts = getTopProducts(5);
        statistics.setTopProducts(topProducts.get("products"));

        return statistics;
    }

    // Thống kê theo khoảng thời gian
    public Map<String, Object> getTimeRangeStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();

        // Lọc các đơn hàng trong khoảng thời gian
        List<Order> ordersInRange = orderRepo.findAll().stream()
                .filter(order -> {
                    LocalDateTime orderDate = order.getCreatedDate(); // Lấy ngày tạo đơn hàng
                    return !orderDate.isBefore(startDate.atStartOfDay()) && !orderDate.isAfter(endDate.atStartOfDay());
                })
                .collect(Collectors.toList());

        // Doanh thu theo ngày
        Map<String, BigDecimal> revenueByDate = new HashMap<>();

        // Số đơn hàng theo ngày
        Map<String, Integer> ordersByDate = new HashMap<>();

        // Doanh thu theo trạng thái
        Map<String, BigDecimal> revenueByStatus = new HashMap<>();
        revenueByStatus.put("APPROVED", BigDecimal.ZERO);
        revenueByStatus.put("PENDING", BigDecimal.ZERO);
        revenueByStatus.put("REJECTED", BigDecimal.ZERO);

        // Thống kê dữ liệu
        for (Order order : ordersInRange) {
            String dateKey = order.getCreatedDate().toString(); // Sử dụng ngày tạo đơn hàng

            // Doanh thu theo ngày
            revenueByDate.put(dateKey, revenueByDate.getOrDefault(dateKey, BigDecimal.ZERO).add(order.getTotal()));

            // Số đơn hàng theo ngày
            ordersByDate.put(dateKey, ordersByDate.getOrDefault(dateKey, 0) + 1);

            // Doanh thu theo trạng thái
            String status = order.getStatus().toString();
            revenueByStatus.put(status, revenueByStatus.getOrDefault(status, BigDecimal.ZERO).add(order.getTotal()));
        }

        result.put("revenueByDate", revenueByDate);
        result.put("ordersByDate", ordersByDate);
        result.put("revenueByStatus", revenueByStatus);
        result.put("totalOrders", ordersInRange.size());

        // Tổng doanh thu trong khoảng thời gian
        BigDecimal totalRevenue = ordersInRange.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalRevenue", totalRevenue);

        return result;
    }


    // Lấy sản phẩm bán chạy
    public Map<String, Object> getTopProducts(int limit) {
        Map<String, Object> result = new HashMap<>();

        // Lấy tất cả chi tiết đơn hàng
        List<OrderDetail> allOrderDetails = orderDetailRepo.findAll();

        // Tính số lượng bán và doanh thu theo sản phẩm
        Map<String, Integer> productQuantities = new HashMap<>();
        Map<String, BigDecimal> productRevenues = new HashMap<>();

        for (OrderDetail detail : allOrderDetails) {
            String productId = detail.getProduct().getId();

            // Số lượng bán
            productQuantities.put(
                    productId,
                    productQuantities.getOrDefault(productId, 0) + detail.getQuantity()
            );

            // Doanh thu
            BigDecimal itemRevenue = detail.getPrice().multiply(new BigDecimal(detail.getQuantity()));
            productRevenues.put(
                    productId,
                    productRevenues.getOrDefault(productId, BigDecimal.ZERO).add(itemRevenue)
            );
        }

        // Sắp xếp sản phẩm theo số lượng bán
        List<Map.Entry<String, Integer>> sortedByQuantity = new ArrayList<>(productQuantities.entrySet());
        sortedByQuantity.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        // Lấy top sản phẩm
        List<Map<String, Object>> topProducts = new ArrayList<>();

        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedByQuantity) {
            if (count >= limit) break;

            String productId = entry.getKey();
            Product product = productRepo.findById(productId).orElse(null);

            if (product != null) {
                Map<String, Object> productData = new HashMap<>();
                productData.put("id", product.getId());
                productData.put("name", product.getName());
                productData.put("quantity", entry.getValue());
                productData.put("revenue", productRevenues.get(productId));
                productData.put("image", product.getImages() != null && !product.getImages().isEmpty() ? product.getImages().get(0) : null);

                topProducts.add(productData);
                count++;
            }
        }

        result.put("products", topProducts);
        return result;
    }

    public int getPendingOrdersCount() {
        List<Order> allOrders = orderRepo.findAll();

        // Đếm số đơn hàng có trạng thái PENDING
        long pendingOrders = allOrders.stream()
                .filter(order -> order.getStatus() == StatusEnum.PENDING)
                .count();

        return (int) pendingOrders;
    }
}