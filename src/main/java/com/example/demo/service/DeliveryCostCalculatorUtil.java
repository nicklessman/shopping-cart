package com.example.demo.service;

import com.example.demo.constants.ShoppingCartConstants;
import com.example.demo.model.entity.Product;
import com.example.demo.model.ShoppingCart;

import java.util.Map;
import java.util.Set;

public final class DeliveryCostCalculatorUtil {

    public static Double calculate(ShoppingCart shoppingCart) {
        Map<Product, Integer> productToQuantity = shoppingCart.getProductToQuantity();
        long numberOfProducts = productToQuantity.keySet().size();
        long numberOfDeliveries = computeNumberOfDeliveries(productToQuantity.keySet());

        return ShoppingCartConstants.COST_PER_PRODUCT * numberOfProducts + ShoppingCartConstants.COST_PER_DELIVERY * numberOfDeliveries;
    }

    private static long computeNumberOfDeliveries(Set<Product> products) {
        return products.stream()
                .map(Product::getCategory)
                .distinct()
                .count();
    }
}
