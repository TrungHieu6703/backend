package com.example.backend.controller;

import com.example.backend.dto.request.OrderDetailDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.OrderDetailRes;
import com.example.backend.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-details")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    // Tạo mới OrderDetail
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDetailRes>> createOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO) {
        OrderDetailRes orderDetailRes = orderDetailService.createOrderDetail(orderDetailDTO);
        ApiResponse<OrderDetailRes> response = new ApiResponse<>("Order detail created successfully", HttpStatus.CREATED.value(), orderDetailRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật OrderDetail theo ID
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDetailRes>> updateOrderDetail(@PathVariable String id, @RequestBody OrderDetailDTO orderDetailDTO) {
        OrderDetailRes updatedOrderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        ApiResponse<OrderDetailRes> response = new ApiResponse<>("Order detail updated successfully", HttpStatus.OK.value(), updatedOrderDetail);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa OrderDetail theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrderDetail(@PathVariable String id) {
        orderDetailService.deleteOrderDetail(id);
        ApiResponse<Void> response = new ApiResponse<>("Order detail deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy OrderDetail theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDetailRes>> getOrderDetailById(@PathVariable String id) {
        OrderDetailRes orderDetailRes = orderDetailService.getOrderDetailById(id);
        ApiResponse<OrderDetailRes> response = new ApiResponse<>("Order detail retrieved successfully", HttpStatus.OK.value(), orderDetailRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả OrderDetails
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDetailRes>>> getAllOrderDetails() {
        List<OrderDetailRes> orderDetails = orderDetailService.getAllOrderDetails();
        ApiResponse<List<OrderDetailRes>> response = new ApiResponse<>("All order details retrieved successfully", HttpStatus.OK.value(), orderDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
