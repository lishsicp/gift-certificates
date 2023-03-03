package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.ExceptionMessageI18n;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.JsonMapperUtil;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderAssembler orderAssembler;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    @DisplayName("GET /api/orders/{id} - Success")
    void testGetOrderById() throws Exception {
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long orderId = orderDto.getId();
        given(orderService.getById(orderId)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        ResultActions resultActions = mockMvc.perform(get("/api/orders/{id}", orderId));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.cost").exists())
                .andExpect(jsonPath("$.purchaseDate").exists())
                .andExpect(jsonPath("$.giftCertificate").exists())
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - Success")
    void testGetOrdersByUserId() throws Exception {
        int page = 1;
        int size = 5;
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        Page<OrderDto> orders = new PageImpl<>(Collections.singletonList(orderDto));
        PagedModel<OrderDto> pagedOrders = PagedModel.of(
                orders.getContent(),
                new PagedModel.PageMetadata(size, page, orders.getSize())
        );

        given(orderService.getOrdersByUserId(userId, page, size)).willReturn(orders);
        given(orderAssembler.toCollectionModel(orders, page, size, userId)).willReturn(pagedOrders);

        mockMvc.perform(get("/api/orders/users/{id}", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.page.number", is(page)))
                .andExpect(jsonPath("$.page.size", is(size)))
                .andExpect(jsonPath("$.page.totalElements", is(orders.getSize())));
    }

    @Test
    @DisplayName("POST /api/orders - Success")
    void testMakeOrder() throws Exception {
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        long certificateId = orderDto.getGiftCertificate().getId();
        MakeOrderDto makeOrderDto = new MakeOrderDto(certificateId, userId);

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