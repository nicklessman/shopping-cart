package com.example.demo.manager;

import com.example.demo.dto.CreateShoppingCartRequest;
import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.dto.conventer.ShoppingCartDtoConverter;
import com.example.demo.exception.InvalidCouponException;
import com.example.demo.model.entity.Campaign;
import com.example.demo.model.entity.Coupon;
import com.example.demo.model.entity.Product;
import com.example.demo.model.ShoppingCart;
import com.example.demo.service.CouponService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ShoppingCartService;
import com.example.demo.service.strategy.CampaignDiscountStrategy;
import com.example.demo.service.strategy.CouponDiscountStrategy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShoppingCartManager {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final CouponService couponService;
    private final ShoppingCartDtoConverter conventer;

    public ShoppingCartManager(ShoppingCartService shoppingCartService, ProductService productService, CouponService couponService,
                               ShoppingCartDtoConverter conventer) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.couponService = couponService;
        this.conventer = conventer;
    }

    public ShoppingCartDto process(CreateShoppingCartRequest request) {
        ShoppingCart shoppingCart = buildShoppingCart(request);

        applyCampaign(shoppingCart);
        applyCoupon(shoppingCart, request);
        shoppingCartService.calculateDeliveryCost(shoppingCart);

        return conventer.convert(shoppingCart);
    }

    private void applyCampaign(ShoppingCart shoppingCart) {
        Optional<Campaign> bestSuitableCampaign = shoppingCartService.findBestSuitableCampaign(shoppingCart);
        if (bestSuitableCampaign.isPresent()) {
            shoppingCartService.applyDiscount(shoppingCart, new CampaignDiscountStrategy(bestSuitableCampaign.get()));
            shoppingCart.setCampaignApplied(bestSuitableCampaign.get());
        }
    }

    private void applyCoupon(ShoppingCart shoppingCart, CreateShoppingCartRequest request) {
        if (request.getCouponId() != null) {
            Optional<Coupon> couponOptional = couponService.findCouponById(request.getCouponId());
            if (!couponOptional.isPresent()) {
                throw new InvalidCouponException("Requested coupon was not found or invalidated!");
            }
            boolean success = shoppingCartService.applyDiscount(shoppingCart, new CouponDiscountStrategy(couponOptional.get()));
            shoppingCart.setCouponApplied(success ? couponOptional.get() : null);
        }
    }

    private ShoppingCart buildShoppingCart(CreateShoppingCartRequest request) {
        List<Product> productList = productService.getProductContainIds(
                request.getProductIdToQuantity().keySet()
                        .stream()
                        .collect(Collectors.toList()));

        ShoppingCart shoppingCart = ShoppingCart.builder()
                .productToQuantity(new HashMap<>())
                .totalPrice(0.0)
                .build();

        productList.forEach(product -> {
            shoppingCartService.addProduct(shoppingCart, product, request.getProductIdToQuantity().get(product.getId()));
        });
        return shoppingCart;
    }
}
