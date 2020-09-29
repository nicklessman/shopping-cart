package com.example.demo.controller;

import com.example.demo.dto.CreateShoppingCartRequest;
import com.example.demo.dto.ShoppingCartDto;
import com.example.demo.manager.ShoppingCartManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "server-port=0")
@RunWith(SpringRunner.class)
@DirtiesContext
public class ShoppingCartControllerTest {

    @MockBean
    private ShoppingCartManager shoppingCartManager;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenCreateShoppingCartCalled_ShouldReturnShoppingCartJson() throws Exception {
        Map<Long, Integer> productIdToQuantity = new HashMap<>();
        productIdToQuantity.put(1L, 1);
        CreateShoppingCartRequest request = CreateShoppingCartRequest.builder()
                .productIdToQuantity(productIdToQuantity).build();

        ShoppingCartDto expectedResultDto = ShoppingCartDto.builder().build();

        Mockito.when(shoppingCartManager.process(request)).thenReturn(expectedResultDto);

        this.mockMvc.perform(post("/v1/shopping-cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(expectedResultDto), false))
                .andReturn();
    }
}
