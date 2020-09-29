package com.example.demo.controller;

import com.example.demo.dto.CreateShoppingCartRequest;
import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.manager.ShoppingCartManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartManager shoppingCartManager;

    @PostMapping
    public ResponseEntity<ShoppingCartDto> createShoppingCart(@Valid @RequestBody CreateShoppingCartRequest request) {
        return ResponseEntity.ok(shoppingCartManager.process(request));
    }
}
