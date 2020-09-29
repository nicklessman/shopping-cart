package com.example.demo.dto.conventer;

import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.model.ShoppingCart;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartDtoConverter {

    private final ProductDtoConverter productDtoConverter;

    public ShoppingCartDtoConverter(ProductDtoConverter productDtoConverter) {
        this.productDtoConverter = productDtoConverter;
    }

    public ShoppingCartDto convert(ShoppingCart from) {
        return ShoppingCartDto.builder()
                .totalAmount(String.valueOf(from.getTotalPrice()))
                .deliveryAmount(String.valueOf(from.getDeliveryPrice()))
                .appliedCampaign(from.getCampaignApplied() != null ? from.getCampaignApplied().getTitle() : null)
                .appliedCoupon(from.getCouponApplied() != null ? from.getCouponApplied().getTitle() : null)
                .productList(productDtoConverter.convert(from.getProductToQuantity()))
                .build();
    }
}
