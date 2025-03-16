package com.example.backend.controller;

import com.example.backend.dto.request.CouponDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.CouponRes;
import com.example.backend.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // Tạo mới Coupon
    @PostMapping
    public ResponseEntity<ApiResponse<CouponRes>> createCoupon(@RequestBody CouponDTO couponDTO) {
        CouponRes couponRes = couponService.createCoupon(couponDTO);
        ApiResponse<CouponRes> response = new ApiResponse<>("Coupon created successfully", HttpStatus.CREATED.value(), couponRes);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Cập nhật Coupon theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponRes>> updateCoupon(@PathVariable String id, @RequestBody CouponDTO couponDTO) {
        CouponRes updatedCoupon = couponService.updateCoupon(id, couponDTO);
        ApiResponse<CouponRes> response = new ApiResponse<>("Coupon updated successfully", HttpStatus.OK.value(), updatedCoupon);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xóa Coupon theo ID
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable String id) {
        couponService.deleteCoupon(id);
        ApiResponse<Void> response = new ApiResponse<>("Coupon deleted successfully", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy Coupon theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponRes>> getCouponById(@PathVariable String id) {
        CouponRes couponRes = couponService.getCouponById(id);
        ApiResponse<CouponRes> response = new ApiResponse<>("Coupon retrieved successfully", HttpStatus.OK.value(), couponRes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Lấy tất cả Coupon
    @GetMapping
    public ResponseEntity<ApiResponse<List<CouponRes>>> getAllCoupons() {
        List<CouponRes> coupons = couponService.getAllCoupons();
        ApiResponse<List<CouponRes>> response = new ApiResponse<>("All coupons retrieved successfully", HttpStatus.OK.value(), coupons);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}