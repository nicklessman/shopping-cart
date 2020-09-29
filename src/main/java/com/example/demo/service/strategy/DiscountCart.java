package com.example.demo.service.strategy;

import com.example.demo.model.DiscountFee;

public class DiscountCart {

    private DiscountStrategy discountStrategy;

    public DiscountCart(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public DiscountFee applyDiscountStrategy(int productCount, Double originalPrice) {
        return discountStrategy.applyDiscount(productCount, originalPrice);
    }
}