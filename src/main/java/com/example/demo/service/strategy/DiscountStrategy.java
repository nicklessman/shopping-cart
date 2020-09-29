package com.example.demo.service.strategy;

import com.example.demo.model.DiscountFee;

public interface DiscountStrategy {
    DiscountFee applyDiscount(int productCount, Double originalPrice);
}