package com.example.demo.service;

import com.example.demo.model.entity.Category;
import com.example.demo.model.entity.Product;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.CampaignRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

public class DeliveryCostCalculatorUtilTest {

    private ShoppingCartService shoppingCartService;
    private CampaignService campaignService;
    private CampaignRepository campaignRepository;

    public DeliveryCostCalculatorUtilTest() {
        campaignRepository = Mockito.mock(CampaignRepository.class);
        campaignService = new CampaignService(campaignRepository);
        this.shoppingCartService = new ShoppingCartService(campaignService);
    }

    @Test
    public void whenCalculateCalledShouldReturnAccuratePrice() {
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .totalPrice(0.0)
                .productToQuantity(new HashMap<>())
                .build();

        Category homeCat = new Category();
        homeCat.setTitle("HOME");

        Category clothingCat = new Category();
        clothingCat.setTitle("CLOTHING");

        Product product = new Product();
        product.setCategory(homeCat);
        product.setPrice(15.0);

        shoppingCartService.addProduct(shoppingCart, product, 2);

        product = new Product();
        product.setCategory(clothingCat);
        product.setPrice(30.0);

        shoppingCartService.addProduct(shoppingCart, product, 3);

        Assert.assertEquals((Double) 9.0, DeliveryCostCalculatorUtil.calculate(shoppingCart));
    }
}
