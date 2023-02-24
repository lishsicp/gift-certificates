package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.controller.util.JsonMapperUtil;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.MakeOrderDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderAssembler orderAssembler;

    @Test
    @DisplayName("GET /api/orders/{id} - Success")
    void testGetOrderById() throws Exception {
        long orderId = 1;
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .cost(BigDecimal.valueOf(10.0))
                .purchaseDate(LocalDateTime.now())
                .giftCertificate(new GiftCertificateDto())
                .user(new UserDto())
                .build();

        given(orderService.getById(orderId)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        ResultActions resultActions = mockMvc.perform(get("/api/orders/{id}", orderId));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) orderId)))
                .andExpect(jsonPath("$.cost", is(orderDto.getCost().doubleValue())))
                .andExpect(jsonPath("$.purchaseDate").exists())
                .andExpect(jsonPath("$.giftCertificate").exists())
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - Success")
    void testGetOrdersByUserId() throws Exception {
        long userId = 1;
        int page = 1;
        int size = 5;
        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .cost(BigDecimal.TEN)
                .purchaseDate(LocalDateTime.now())
                .giftCertificate(new GiftCertificateDto())
                .user(new UserDto())
                .build();

        Page<OrderDto> orders = new PageImpl<>(Collections.singletonList(orderDto));
        PagedModel<OrderDto> pagedOrders = PagedModel.of(
                orders.getContent(),
                new PagedModel.PageMetadata(size, page, 0)
        );

        given(orderService.getOrdersByUserId(userId, page, size)).willReturn(orders);
        given(orderAssembler.toCollectionModel(orders, page, size, userId)).willReturn(pagedOrders);

        mockMvc.perform(get("/api/orders/users/{id}", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.orderDtoList").exists())
                .andExpect(jsonPath("$.page.number", is(page)))
                .andExpect(jsonPath("$.page.size", is(size)))
                .andExpect(jsonPath("$.page.totalElements", is(0)));
    }

    @Test
    @DisplayName("POST /api/orders - Success")
    void testMakeOrder() throws Exception {
        long orderId = 1;
        MakeOrderDto makeOrderDto = new MakeOrderDto();
        makeOrderDto.setGiftCertificateId(1);
        makeOrderDto.setUserId(1);
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .cost(BigDecimal.TEN)
                .purchaseDate(LocalDateTime.now())
                .giftCertificate(new GiftCertificateDto())
                .user(new UserDto())
                .build();

        given(orderService.save(makeOrderDto)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapperUtil.asJson(makeOrderDto)))
                .andExpect(status().isCreated());

        then(orderService).should().save(any(OrderDto.class));
        then(orderAssembler).should().toModel(any());
    }
}