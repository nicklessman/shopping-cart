package com.example.demo.manager;

import com.example.demo.dto.CreateShoppingCartRequest;
import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.dto.conventer.ProductDtoConverter;
import com.example.demo.dto.conventer.ShoppingCartDtoConverter;
import com.example.demo.exception.InvalidCouponException;
import com.example.demo.model.entity.*;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.CouponRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CampaignService;
import com.example.demo.service.CouponService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ShoppingCartService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;


public class ShoppingCartManagerTest {

    private ShoppingCartManager shoppingCartManager;
    private ShoppingCartService shoppingCartService;
    private CampaignService campaignService;
    private ProductService productService;
    private CouponService couponService;
    private CampaignRepository campaignRepository;
    private ProductRepository productRepository;
    private CouponRepository couponRepository;

    public ShoppingCartManagerTest() {
        couponRepository = Mockito.mock(CouponRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        campaignRepository = Mockito.mock(CampaignRepository.class);

        couponService = new CouponService(couponRepository);
        productService = new ProductService(productRepository);
        campaignService = new CampaignService(campaignRepository);
        shoppingCartService = new ShoppingCartService(campaignService);

        shoppingCartManager = new ShoppingCartManager(shoppingCartService, productService, couponService, new ShoppingCartDtoConverter(new ProductDtoConverter()));
    }

    @Test(expected = InvalidCouponException.class)
    public void whenApplyCouponCalledWithInvalidOrNotExistingCoupon_ShouldThrowInvalidCouponException() {

        shoppingCartManager.process(CreateShoppingCartRequest.builder().productIdToQuantity(new HashMap<>()).couponId(-1L).build());
    }

    @Test
    public void whenProcessCalledWithCoupon_ShouldApplyCoupon() {

        Coupon coupon = new Coupon();
        coupon.setDiscountType("amount");
        coupon.setDiscountAmount(200.0);
        coupon.setMinCartAmount(250.0);
        coupon.setTitle("200 TL GIFT!");

        Category category = new Category();
        category.setId(1L);
        category.setTitle("CLOTHING");

        Product product = new Product();
        product.setPrice(700.0);
        product.setId(1L);
        product.setTitle("MILLA SUIT");
        product.setCategory(category);

        Map<Long, Integer> productIdToQuantity = new HashMap<>();
        productIdToQuantity.put(1L, 1);
        Mockito.when(couponService.findCouponById(1L))
                .thenReturn(Optional.of(coupon));

        Mockito.when(productService.getProductContainIds(productIdToQuantity.keySet().stream().collect(Collectors.toList())))
                .thenReturn(Arrays.asList(product));

        CreateShoppingCartRequest request = CreateShoppingCartRequest.builder()
                .productIdToQuantity(productIdToQuantity)
                .couponId(1L)
                .build();

        ShoppingCartDto shoppingCartDto = shoppingCartManager.process(request);

        Assert.assertEquals(coupon.getTitle(), shoppingCartDto.getAppliedCoupon());
    }

    @Test
    public void whenProcessCalled_ShouldFindBestCampaignAndApply() {
        Category category = new Category();
        category.setId(1L);
        category.setTitle("CLOTHING");

        Product product = new Product();
        product.setPrice(700.0);
        product.setId(1L);
        product.setTitle("MILLA SUIT");
        product.setCategory(category);

        Campaign campaign = new Campaign();
        campaign.setMinCartProductNumber(1);
        campaign.setDiscountType("rate");
        campaign.setDiscountAmount(50.0);
        campaign.setCategory(category);
        campaign.setTitle("%50 SALE!");

        Map<Long, Integer> productIdToQuantity = new HashMap<>();
        productIdToQuantity.put(1L, 1);

        Mockito.when(productService.getProductContainIds(productIdToQuantity.keySet().stream().collect(Collectors.toList())))
                .thenReturn(Arrays.asList(product));

        CreateShoppingCartRequest request = CreateShoppingCartRequest.builder()
                .productIdToQuantity(productIdToQuantity)
                .build();

        Mockito.when(campaignService.getAllCampaigns()).thenReturn(Arrays.asList(campaign));

        ShoppingCartDto shoppingCartDto = shoppingCartManager.process(request);

        Assert.assertEquals(campaign.getTitle(), shoppingCartDto.getAppliedCampaign());
    }
}
