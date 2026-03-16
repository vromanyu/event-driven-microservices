package org.vromanyu.order.controller;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.vromanyu.core.OrderRequestDto;
import org.vromanyu.core.OrderStatus;
import org.vromanyu.order.entity.Order;
import org.vromanyu.order.service.OrderService;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;

@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void whenCreateOrder_thenSuccess() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto(1, 1, 1);

        Order createdOrder = new Order();
        createdOrder.setId(1);
        createdOrder.setUserId(1);
        createdOrder.setProductId(1);
        createdOrder.setPrice(1);
        createdOrder.setOrderStatus(OrderStatus.CREATED);
        createdOrder.setCreatedAt(LocalDateTime.now());
        createdOrder.setUpdatedAt(LocalDateTime.now());

        Mockito.when(orderService.createOrder(Mockito.any())).thenReturn(createdOrder);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonMapper.writeValueAsString(orderRequestDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(createdOrder.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", CoreMatchers.is(createdOrder.getUserId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productId", CoreMatchers.is(createdOrder.getProductId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", CoreMatchers.is(createdOrder.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderStatus", CoreMatchers.is(createdOrder.getOrderStatus().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.paymentStatus", CoreMatchers.nullValue()));
    }

    @Test
    public void whenGetOrderByInvalidId_thenFail() throws Exception {
        Order createdOrder = new Order();
        createdOrder.setId(1);
        createdOrder.setUserId(1);
        createdOrder.setProductId(1);
        createdOrder.setPrice(1);
        createdOrder.setOrderStatus(OrderStatus.CREATED);
        createdOrder.setCreatedAt(LocalDateTime.now());
        createdOrder.setUpdatedAt(LocalDateTime.now());

        Mockito.when(orderService.getOrder(Mockito.anyInt())).thenThrow(new RuntimeException("order not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/{id}", createdOrder.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.notNullValue()));
    }

}
