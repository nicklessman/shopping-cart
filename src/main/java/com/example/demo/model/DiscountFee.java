package com.example.demo.model;

import com.example.demo.model.enums.DiscountType;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DiscountFee {
    private Double discount;
    private DiscountType discountType;
}
