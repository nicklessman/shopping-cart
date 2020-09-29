package com.example.demo.service.strategy;

import com.example.demo.model.DiscountFee;
import com.example.demo.model.entity.Campaign;
import com.example.demo.model.enums.DiscountType;

public class CampaignDiscountStrategy implements DiscountStrategy {

    private final Campaign campaign;

    public CampaignDiscountStrategy(Campaign campaign) {
        this.campaign = campaign;
    }

    @Override
    public DiscountFee applyDiscount(int productCount, Double originalPrice) {
        if (productCount < campaign.getMinCartProductNumber()) {
            return DiscountFee.builder()
                    .discount(0.0)
                    .discountType(DiscountType.NOTELIGIBLE)
                    .build();
        }

        if (DiscountType.AMOUNT.equals(DiscountType.find(campaign.getDiscountType()))) {
            return DiscountFee.builder()
                    .discount(originalPrice - campaign.getDiscountAmount())
                    .discountType(DiscountType.AMOUNT).build();
        } else {
            return DiscountFee.builder()
                    .discount(originalPrice * campaign.getDiscountAmount() / 100)
                    .discountType(DiscountType.RATE).build();
        }
    }
}
