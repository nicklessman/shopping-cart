package com.example.demo.service;

import com.example.demo.model.ShoppingCart;
import com.example.demo.model.entity.*;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.service.strategy.CampaignDiscountStrategy;
import com.example.demo.service.strategy.CouponDiscountStrategy;
import com.example.demo.service.strategy.DiscountStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ShoppingCartServiceTest {


    private CampaignRepository campaignRepository;
    private CampaignService campaignService;
    private ShoppingCartService shoppingCartService;

    @Before
    public void setup() {
        campaignRepository = Mockito.mock(CampaignRepository.class);
        campaignService = new CampaignService(campaignRepository);
        shoppingCartService = new ShoppingCartService(campaignService);
    }

    @Test
    public void whenAddProductCalled_ShouldAddProductsAndUpdatePrice() {
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .totalPrice(0.0)
                .productToQuantity(new HashMap<>())
                .build();
        Product product = new Product();
        product.setTitle("SWEAT");
        product.setPrice(110.00);
        Category category = new Category();
        category.setTitle("CLOTHING");
        product.setCategory(category);

        shoppingCartService.addProduct(shoppingCart, product, 2);

        Assert.assertEquals(shoppingCart.getTotalPrice(), (Double) 220.00);
    }

    @Test
    public void whenApplyDiscountCalledForCampaignDiscount_ShouldReturnTrue() {
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .totalPrice(0.0)
                .productToQuantity(new HashMap<>())
                .build();
        Product product = new Product();
        product.setTitle("SWEAT");
        product.setPrice(110.00);
        Category category = new Category();
        category.setTitle("CLOTHING");
        product.setCategory(category);

        Campaign campaign = new Campaign();
        campaign.setCategory(category);
        campaign.setMinCartProductNumber(1);
        campaign.setDiscountAmount(50.0);
        campaign.setDiscountType("rate");

        DiscountStrategy discountStrategy = new CampaignDiscountStrategy(campaign);
        shoppingCartService.addProduct(shoppingCart, product, 1);

        boolean result = shoppingCartService.applyDiscount(shoppingCart, discountStrategy);

        Assert.assertTrue(result);
        Assert.assertEquals((Double) 55.0, shoppingCart.getTotalPrice());
    }

    @Test
    public void whenAppleDiscountCalledForCouponDiscount_ShouldReturnTrue() {
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .totalPrice(0.0)
                .productToQuantity(new HashMap<>())
                .build();
        Product product = new Product();
        product.setTitle("SWEATER");
        product.setPrice(110.00);
        Category category = new Category();
        category.setTitle("CLOTHING");
        product.setCategory(category);

        Coupon coupon = new Coupon();
        coupon.setDiscountAmount(250.0);
        coupon.setMinCartAmount(300.0);
        coupon.setDiscountType("amount");

        DiscountStrategy discountStrategy = new CouponDiscountStrategy(coupon);
        shoppingCartService.addProduct(shoppingCart, product, 3);

        boolean result = shoppingCartService.applyDiscount(shoppingCart, discountStrategy);

        Assert.assertTrue(result);
        Assert.assertEquals((Double) 80.0, shoppingCart.getTotalPrice());
    }

    @Test
    public void whenFindBestSuitableCampaignCalled_ShouldReturnTheBest() {
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .totalPrice(0.0)
                .productToQuantity(new HashMap<>())
                .build();

        Category category = new Category();
        category.setTitle("CLOTHING");

        Category childCategory = new Category();
        childCategory.setParent(category);
        childCategory.setTitle("SWEATS");

        Product product = new Product();
        product.setTitle("MILLA SWEATER");
        product.setPrice(110.00);
        product.setCategory(childCategory);

        // Cannot be applied in order to minimum cart product number
        List<Campaign> campaigns = new ArrayList<>();
        Campaign campaign = new Campaign();
        campaign.setDiscountType("rate");
        campaign.setMinCartProductNumber(3);
        campaign.setDiscountAmount(75.0);
        campaign.setCategory(category);
        campaigns.add(campaign);

        // Best fit
        Campaign campaignLower = new Campaign();
        campaignLower.setDiscountType("rate");
        campaignLower.setMinCartProductNumber(1);
        campaignLower.setDiscountAmount(50.0);
        campaignLower.setCategory(category);
        campaigns.add(campaignLower);

        Mockito.when(campaignRepository.findAll()).thenReturn(campaigns);

        shoppingCartService.addProduct(shoppingCart, product, 2);
        Optional<Campaign> campaignOptional = shoppingCartService.findBestSuitableCampaign(shoppingCart);

        Assert.assertEquals(campaignLower, campaignOptional.get());
    }

    @Test
    public void whenFindBestSuitableCampaignCalledAndCampaignCategoryParentMatches_shouldReturnTheCampaign() {
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .totalPrice(0.0)
                .productToQuantity(new HashMap<>())
                .build();

        Category parentCategory = new Category();
        parentCategory.setTitle("HOME");

        Category childCategory = new Category();
        childCategory.setParent(parentCategory);
        childCategory.setTitle("BEDDING");

        Product product = new Product();
        product.setTitle("PILLOW");
        product.setPrice(110.00);
        product.setCategory(childCategory);

        List<Campaign> campaigns = new ArrayList<>();
        Campaign campaign = new Campaign();
        campaign.setDiscountType("rate");
        campaign.setMinCartProductNumber(1);
        campaign.setDiscountAmount(50.0);
        campaign.setCategory(parentCategory);
        campaigns.add(campaign);

        Mockito.when(campaignRepository.findAll()).thenReturn(campaigns);

        shoppingCartService.addProduct(shoppingCart, product, 3);
        Optional<Campaign> campaignOptional = shoppingCartService.findBestSuitableCampaign(shoppingCart);

        Assert.assertTrue(campaignOptional.isPresent());
        Assert.assertEquals(campaign, campaignOptional.get());
    }

    @Test
    public void whenCalculateDeliveryCostCalled_ShouldReturnAccurateAmount() {
        ShoppingCart shoppingCart = ShoppingCart
                .builder()
                .totalPrice(0.0)
                .productToQuantity(new HashMap<>())
                .build();

        Category category = new Category();
        category.setTitle("CLOTHING");

        Product product = new Product();
        product.setTitle("SWEAT A BRAND");
        product.setPrice(350.0);
        product.setCategory(category);
        shoppingCartService.addProduct(shoppingCart, product, 1);

        product = new Product();
        product.setTitle("MILLA SWEATER HODIE");
        product.setPrice(50.0);
        product.setCategory(category);
        shoppingCartService.addProduct(shoppingCart, product, 2);

        category = new Category();
        category.setTitle("HOME");

        product = new Product();
        product.setTitle("COFFEE MUG");
        product.setPrice(20.0);
        product.setCategory(category);
        shoppingCartService.addProduct(shoppingCart, product, 1);

        shoppingCartService.calculateDeliveryCost(shoppingCart);

        Assert.assertEquals((Double) 11.0, shoppingCart.getDeliveryPrice());
    }
}
