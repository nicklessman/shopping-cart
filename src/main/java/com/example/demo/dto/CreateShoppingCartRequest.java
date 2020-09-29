package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class CreateShoppingCartRequest {
    @NotEmpty(message = "Products are mandatory. Shopping Cart cannot be created without product.")
    private Map<Long, Integer> productIdToQuantity;
    private Long couponId;
}
