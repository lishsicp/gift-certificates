package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.JsonMapperUtil;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = OrderController.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderAssembler orderAssembler;

    @Test
    @DisplayName("GET /api/orders/{id} - Success")
    void getOrderById_shouldReturnOrder() throws Exception {
        // given
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long orderId = orderDto.getId();
        given(orderService.getById(orderId)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        // when
        ResultActions resultActions = mockMvc
            .perform(get("/api/orders/{id}", orderId));

        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price").exists())
            .andExpect(jsonPath("$.purchaseDate").exists())
            .andExpect(jsonPath("$.giftCertificate").exists())
            .andExpect(jsonPath("$.user").exists());

        // then
        then(orderService).should().getById(anyLong());
        then(orderAssembler).should().toModel(any(OrderDto.class));
    }

    @Test
    @DisplayName("GET /api/orders - Success")
    void getAllOrders_shouldReturnOrderList() throws Exception {
        // given
        int page = 1;
        int size = 5;
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        Page<OrderDto> orders = new PageImpl<>(Collections.singletonList(orderDto));
        PagedModel<OrderDto> pagedOrders = PagedModel.of(
            orders.getContent(),
            new PagedModel.PageMetadata(size, page, orders.getSize())
        );

        given(orderService.getAll(page, size)).willReturn(orders);
        given(orderAssembler.toCollectionModel(any(), any(Link.class))).willReturn(pagedOrders);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/orders")
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.orderDtoList").exists())
            .andExpect(jsonPath("$.page.number", is(page)))
            .andExpect(jsonPath("$.page.size", is(size)))
            .andExpect(jsonPath("$.page.totalElements", is(orders.getSize())));

        // then
        then(orderService).should().getAll(anyInt(), anyInt());
        then(orderAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - Success")
    void getOrdersByUserId_shouldReturnOrderList() throws Exception {
        // given
        int page = 1;
        int size = 5;
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        Page<OrderDto> orders = new PageImpl<>(Collections.singletonList(orderDto));
        PagedModel<OrderDto> pagedOrders = PagedModel.of(
            orders.getContent(),
            new PagedModel.PageMetadata(size, page, orders.getSize())
        );

        given(orderService.getOrdersByUserId(page, size, userId)).willReturn(orders);
        given(orderAssembler.toCollectionModel(any(), any(Link.class))).willReturn(pagedOrders);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/orders/users/{id}", userId)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .contentType(MediaType.APPLICATION_JSON_VALUE));
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.orderDtoList").exists())
            .andExpect(jsonPath("$.page.number", is(page)))
            .andExpect(jsonPath("$.page.size", is(size)))
            .andExpect(jsonPath("$.page.totalElements", is(orders.getSize())));

        // then
        then(orderService).should().getOrdersByUserId(anyInt(), anyInt(), anyLong());
        then(orderAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("POST /api/orders - Success")
    void saveOrder_shouldReturnSavedOrder() throws Exception {
        // given
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        long certificateId = orderDto.getGiftCertificate().getId();
        MakeOrderDto makeOrderDto = new MakeOrderDto(certificateId, userId);

        given(orderService.save(makeOrderDto)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(JsonMapperUtil.asJson(makeOrderDto)));

        resultActions
            .andExpect(status().isCreated());

        // then
        then(orderService).should().save(any(OrderDto.class));
        then(orderAssembler).should().toModel(any());
    }

    @Test
    @DisplayName("DELETE /api/orders/{id} - Success")
    void deleteOrder_shouldDelete() throws Exception {
        // given
        long id = 1;

        // when
        mockMvc.perform(delete("/api/orders/{id}", id))
            .andExpect(status().isNoContent());

        // then
        then(orderService).should().delete(id);
    }
}