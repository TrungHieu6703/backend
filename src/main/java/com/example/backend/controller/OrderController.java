package com.example.backend.controller;

import com.example.backend.dto.request.OrderDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.OrderRes;
import com.example.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Tạo mới Order
    @PostMapping
    public ResponseEntity<ApiResponse<OrderRes>> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderRes orderRes = orderService.createOrder(orderDTO);
        ApiResponse<OrderRes> response = new ApiResponse<>("Order created successfully", HttpStatus.CREATED.value(), orderRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật Order theo ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderRes>> updateOrder(@PathVariable String id, @RequestBody OrderDTO orderDTO) {
        OrderRes updatedOrder = orderService.updateOrder(id, orderDTO);
        ApiResponse<OrderRes> response = new ApiResponse<>("Order updated successfully", HttpStatus.OK.value(), updatedOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa Order theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        ApiResponse<Void> response = new ApiResponse<>("Order deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy Order theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderRes>> getOrderById(@PathVariable String id) {
        OrderRes orderRes = orderService.getOrderById(id);
        ApiResponse<OrderRes> response = new ApiResponse<>("Order retrieved successfully", HttpStatus.OK.value(), orderRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả Orders
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderRes>>> getAllOrders() {
        List<OrderRes> orders = orderService.getAllOrders();
        ApiResponse<List<OrderRes>> response = new ApiResponse<>("All orders retrieved successfully", HttpStatus.OK.value(), orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
