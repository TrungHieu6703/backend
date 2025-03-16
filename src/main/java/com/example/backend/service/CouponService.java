package com.example.backend.service;

import com.example.backend.dto.request.CouponDTO;
import com.example.backend.dto.response.CouponRes;
import com.example.backend.entity.Coupon;
import com.example.backend.repository.CouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {
    @Autowired
    private CouponRepo couponRepo;

    public CouponRes createCoupon(CouponDTO CouponDTO) {
        Coupon Coupon = new Coupon();
        Coupon.setDiscount(CouponDTO.getDiscount());

        Coupon result = couponRepo.save(Coupon);

        return new CouponRes(result.getId(), result.getDiscount());
    }

    public CouponRes updateCoupon(String id ,CouponDTO CouponDTO) {
        Coupon Coupon = couponRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Coupon not found")));

        Coupon.setDiscount(CouponDTO.getDiscount());

        Coupon updatedCoupon = couponRepo.save(Coupon);

        return new CouponRes(updatedCoupon.getId(), updatedCoupon.getDiscount());
    }

    public void deleteCoupon(String id) {
        Coupon Coupon = couponRepo.findById(id).orElseThrow(()-> new RuntimeException(("Coupon not found")));
        couponRepo.delete(Coupon);
    }

    public CouponRes getCouponById(String id) {
        Coupon Coupon = couponRepo.findById(id)
                .orElseThrow(()-> new RuntimeException(("Coupon not found")));
        return new CouponRes(
                Coupon.getId(),
                Coupon.getDiscount()
        );
    }

    public List<CouponRes> getAllCoupons() {
        List<Coupon> Coupons = couponRepo.findAll();

        return Coupons.stream()
                .map(Coupon -> new CouponRes(
                        Coupon.getId(),
                        Coupon.getDiscount()
                )).collect(Collectors.toList());
    }
}
