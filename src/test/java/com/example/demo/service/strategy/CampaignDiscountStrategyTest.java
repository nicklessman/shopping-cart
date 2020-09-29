package com.example.demo.service.strategy;

import com.example.demo.model.DiscountFee;
import com.example.demo.model.entity.Campaign;
import com.example.demo.model.enums.DiscountType;
import org.junit.Assert;
import org.junit.Test;

public class CampaignDiscountStrategyTest {

    @Test
    public void whenApplyDiscountCalled_ShouldReturnExpectedDiscountFee(){
        Campaign campaign = new Campaign();
        campaign.setDiscountAmount(50.0);
        campaign.setDiscountType("rate");
        campaign.setMinCartProductNumber(3);
        DiscountStrategy discountStrategy = new CampaignDiscountStrategy(campaign);
        DiscountFee actual = discountStrategy.applyDiscount(5, 160.0);

        DiscountFee expected = DiscountFee.builder()
                .discount(80.0)
                .discountType(DiscountType.RATE)
                .build();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenApplyDiscountCalledMinCartProductNumberNotProvided_ShouldReturnNotEligibleDiscountFee() {
        Campaign campaign = new Campaign();
        campaign.setDiscountAmount(50.0);
        campaign.setDiscountType("amount");
        campaign.setMinCartProductNumber(3);
        DiscountStrategy discountStrategy = new CampaignDiscountStrategy(campaign);

        DiscountFee actual = discountStrategy.applyDiscount(2, 160.0);

        DiscountFee expected = DiscountFee.builder()
                .discount(0.0)
                .discountType(DiscountType.NOTELIGIBLE)
                .build();

        Assert.assertEquals(expected, actual);
    }
}
