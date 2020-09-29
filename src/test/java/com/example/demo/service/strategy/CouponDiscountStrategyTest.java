package com.example.demo.service.strategy;

import com.example.demo.model.DiscountFee;
import com.example.demo.model.entity.Coupon;
import com.example.demo.model.enums.DiscountType;
import org.junit.Assert;
import org.junit.Test;

public class CouponDiscountStrategyTest {

    @Test
    public void whenApplyDiscountCalled_ShouldReturnExpectedDiscountFee() {
        Coupon coupon = new Coupon();
        coupon.setMinCartAmount(100.0);
        coupon.setDiscountType("amount");
        coupon.setDiscountAmount(50.0);

        DiscountStrategy discountStrategy = new CouponDiscountStrategy(coupon);
        DiscountFee actual = discountStrategy.applyDiscount(5, 160.0);

        DiscountFee expected = DiscountFee.builder()
                .discount(50.0)
                .discountType(DiscountType.AMOUNT)
                .build();

        Assert.assertEquals(expected, actual);
    }
}
