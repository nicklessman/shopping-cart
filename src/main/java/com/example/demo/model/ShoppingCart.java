package com.example.demo.model;

import com.example.demo.model.entity.Campaign;
import com.example.demo.model.entity.Coupon;
import com.example.demo.model.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ShoppingCart {
    private Map<Product, Integer> productToQuantity;
    private Campaign campaignApplied;
    private Coupon couponApplied;
    private Double totalPrice;
    private Double deliveryPrice;
}