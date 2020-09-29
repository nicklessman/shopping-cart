package com.example.demo.service;

import com.example.demo.model.entity.Coupon;
import com.example.demo.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Optional<Coupon> findCouponById(Long id) {
        return couponRepository.findById(id);
    }
}
