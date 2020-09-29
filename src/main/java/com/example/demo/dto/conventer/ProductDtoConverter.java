package com.example.demo.dto.conventer;

import com.example.demo.dto.ProductDto;
import com.example.demo.model.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductDtoConverter {

    public ProductDto convert(Product from, int quantity) {
        return ProductDto.builder()
                .quantity(String.valueOf(quantity))
                .title(from.getTitle())
                .price(String.valueOf(from.getPrice()))
                .build();
    }

    public List<ProductDto> convert(Map<Product, Integer> productToQuantity) {
        return productToQuantity.entrySet().stream().map(entry -> ProductDto.builder()
                .quantity(String.valueOf(entry.getValue()))
                .title(entry.getKey().getTitle())
                .price(String.valueOf(entry.getKey().getPrice()))
                .build()).collect(Collectors.toList());
    }
}
