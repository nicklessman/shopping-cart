package com.example.demo.service.strategy;

import com.example.demo.model.DiscountFee;
import com.example.demo.model.entity.Coupon;
import com.example.demo.model.enums.DiscountType;

public class CouponDiscountStrategy implements DiscountStrategy {

    private final Coupon coupon;

    public CouponDiscountStrategy(Coupon coupon) {
        this.coupon = coupon;
    }

    @Override
    public DiscountFee applyDiscount(int productCount, Double originalPrice) {
        if (originalPrice.compareTo(coupon.getMinCartAmount()) < 0) {
            return DiscountFee.builder()
                    .discount(0.0)
                    .discountType(DiscountType.NOTELIGIBLE)
                    .build();
        }
        if (DiscountType.AMOUNT.equals(DiscountType.find(coupon.getDiscountType()))) {
            return DiscountFee.builder()
                    .discount(coupon.getDiscountAmount())
                    .discountType(DiscountType.AMOUNT).build();
        } else {
            return DiscountFee.builder()
                    .discount(originalPrice * coupon.getDiscountAmount() / 100)
                    .discountType(DiscountType.RATE)
                    .build();
        }
    }
}
