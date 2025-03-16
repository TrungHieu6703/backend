package com.example.backend.repository;

import com.example.backend.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepo extends JpaRepository<Coupon, String> {
}
