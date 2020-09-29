package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ShoppingCartDto {
    private String totalAmount;
    private String deliveryAmount;
    private String appliedCampaign;
    private String appliedCoupon;
    private List<ProductDto> productList;
}