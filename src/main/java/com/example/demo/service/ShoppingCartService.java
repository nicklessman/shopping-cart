package com.example.demo.service;

import com.example.demo.model.DiscountFee;
import com.example.demo.model.entity.Campaign;
import com.example.demo.model.entity.Category;
import com.example.demo.model.entity.Product;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.enums.DiscountType;
import com.example.demo.service.strategy.CampaignDiscountStrategy;
import com.example.demo.service.strategy.DiscountCart;
import com.example.demo.service.strategy.DiscountStrategy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final CampaignService campaignService;

    public ShoppingCartService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public void addProduct(ShoppingCart shoppingCart, Product product, int quantity) {
        Map<Product, Integer> productToQuantity = shoppingCart.getProductToQuantity();
        productToQuantity.compute(product, (key, val) -> val != null ? val + quantity : quantity);
        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() + product.getPrice() * quantity);
    }

    public boolean applyDiscount(ShoppingCart shoppingCart, DiscountStrategy discountStrategy) {
        DiscountFee discountFee = calculateDiscount(shoppingCart, discountStrategy);

        if (!DiscountType.NOTELIGIBLE.equals(discountFee.getDiscountType())) {
            shoppingCart.setTotalPrice(shoppingCart.getTotalPrice() - discountFee.getDiscount());
            return true;
        }
        return false;
    }

    public Optional<Campaign> findBestSuitableCampaign(ShoppingCart shoppingCart) {
        Optional<Campaign> campaignOptional = Optional.empty();
        List<Campaign> suitableCampaignsForShoppingCart = getSuitableCampaigns(shoppingCart);

        Map<Campaign, Double> campaignToDiscountAmount = new HashMap<>();
        suitableCampaignsForShoppingCart.forEach(campaign -> {
            DiscountStrategy discountStrategy = new CampaignDiscountStrategy(campaign);
            DiscountFee discountFee = new DiscountCart(discountStrategy).applyDiscountStrategy(calculateProductCount(shoppingCart), shoppingCart.getTotalPrice());
            if (!DiscountType.NOTELIGIBLE.equals(discountFee.getDiscountType())) {
                campaignToDiscountAmount.put(campaign, discountFee.getDiscount());
            }
        });

        Optional<Map.Entry<Campaign, Double>> campaignDoubleEntryOptional = campaignToDiscountAmount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        if (campaignDoubleEntryOptional.isPresent()) {
            campaignOptional = Optional.of(campaignDoubleEntryOptional.get().getKey());
        }
        return campaignOptional;
    }

    public void calculateDeliveryCost(ShoppingCart shoppingCart) {
        shoppingCart.setDeliveryPrice(DeliveryCostCalculatorUtil.calculate(shoppingCart));
    }

    private List<Campaign> getSuitableCampaigns(ShoppingCart shoppingCart) {
        List<Campaign> campaignList = campaignService.getAllCampaigns();

        List<Category> categories = new ArrayList<>();
        shoppingCart.getProductToQuantity().keySet().forEach(product -> {
            collectParentCategories(product.getCategory(), categories, 3);
        });

        return campaignList
                .stream()
                .filter(f -> categories.contains(f.getCategory()))
                .collect(Collectors.toList());
    }

    private void collectParentCategories(Category category, List<Category> result, int maxDepth) {
        if (category.getParent() != null) {
            Category parent = category.getParent();
            if (maxDepth > 0 && category.getParent() != null) {
                maxDepth--;
                collectParentCategories(parent, result, maxDepth);
            }
        }
        result.add(category);
    }

    private DiscountFee calculateDiscount(ShoppingCart shoppingCart, DiscountStrategy discountStrategy) {
        return new DiscountCart(discountStrategy).applyDiscountStrategy(calculateProductCount(shoppingCart), shoppingCart.getTotalPrice());
    }

    private int calculateProductCount(ShoppingCart shoppingCart) {
        return shoppingCart.getProductToQuantity()
                .values()
                .stream()
                .reduce(0, Integer::sum);
    }
}