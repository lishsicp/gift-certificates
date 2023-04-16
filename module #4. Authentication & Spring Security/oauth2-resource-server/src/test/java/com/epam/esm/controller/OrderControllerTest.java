package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.config.TestMethodSecurityConfiguration;
import com.epam.esm.config.TestSecurityConfig;
import com.epam.esm.config.UserIdPermissionEvaluator;
import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static com.epam.esm.util.JsonMapperUtil.asJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import({TestMethodSecurityConfiguration.class, TestSecurityConfig.class})
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

    @MockBean
    private UserIdPermissionEvaluator userIdPermissionEvaluator;

    @Test
    @DisplayName("GET /api/orders/{id} - should return order when admin user with scope 'order.read' gets order")
    void getById_shouldReturnOrder_whenAdminUserGetsOrder() throws Exception {
        // given
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long orderId = orderDto.getId();
        given(orderService.getById(orderId)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/orders/{id}", orderId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_order.read"))));

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.price").exists())
            .andExpect(jsonPath("$.purchaseDate").exists())
            .andExpect(jsonPath("$.giftCertificate").exists())
            .andExpect(jsonPath("$.user").exists());

        // then
        then(orderService).should().getById(anyLong());
        then(orderAssembler).should().toModel(any(OrderDto.class));
    }

    @Test
    @DisplayName("GET /api/orders/{id} - should respond with forbidden status code when admin user without 'order.read' scope gets order")
    void getById_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        long orderId = 1;

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/orders/{id}", orderId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(jwt().authorities(createAuthorityList("ROLE_ADMIN"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders/{id} - should respond with forbidden status code when user with insufficient role gets order")
    void getById_shouldRespondWithForbiddenStatusCode_whenUserWithInsufficientRoleGetsOrders() throws Exception {
        // given
        long orderId = 1;

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/orders/{id}", orderId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_order.read"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders/{id} - should respond with unauthorized status code when user is not authenticated")
    void getById_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        long orderId = 1;

        // When / Then
        mockMvc.perform(get("/api/orders/{id}", orderId).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isUnauthorized());

        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders - should return orders when admin user with scope 'order.read' gets orders")
    void getAll_shouldReturnOrders_whenAdminUserGetsOrders() throws Exception {
        // given
        int page = 1;
        int size = 5;
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        Page<OrderDto> orders = new PageImpl<>(Collections.singletonList(orderDto));
        PagedModel<OrderDto> pagedOrders =
            PagedModel.of(orders.getContent(), new PagedModel.PageMetadata(size, page, orders.getSize()));

        given(orderService.getAll(page, size)).willReturn(orders);
        given(orderAssembler.toCollectionModel(any(), any(Link.class))).willReturn(pagedOrders);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/orders").with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_order.read")))
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.orderDtoList").exists())
            .andExpect(jsonPath("$.page.number", is(page)))
            .andExpect(jsonPath("$.page.size", is(size)))
            .andExpect(jsonPath("$.page.totalElements", is(orders.getSize())));

        then(orderService).should().getAll(anyInt(), anyInt());
        then(orderAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/orders - should respond with forbidden status code when admin user without 'order.read' scope gets orders")
    void getAll_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE)
            .with(jwt().authorities(createAuthorityList("ROLE_ADMIN"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders - should respond with forbidden status code when user with insufficient role gets orders")
    void getAll_shouldRespondWithForbiddenStatusCode_whenUserWithInsufficientRoleGetsUser() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE)
            .with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_order.read"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders - should respond with unauthorized status code when user is not authenticated")
    void getAll_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // When / Then
        mockMvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isUnauthorized());

        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - should return orders when admin user with scope 'order.read' gets users orders")
    void getByUserId_shouldReturnOrders_whenAdminUserGetsUsersOrders() throws Exception {
        // given
        int page = 1;
        int size = 5;
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        Page<OrderDto> orders = new PageImpl<>(Collections.singletonList(orderDto));
        PagedModel<OrderDto> pagedOrders =
            PagedModel.of(orders.getContent(), new PagedModel.PageMetadata(size, page, orders.getSize()));
        given(orderService.getOrdersByUserId(page, size, userId)).willReturn(orders);
        given(orderAssembler.toCollectionModel(any(), any(Link.class))).willReturn(pagedOrders);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/orders/users/{id}", userId).with(
                jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_order.read")))
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.orderDtoList").exists())
            .andExpect(jsonPath("$.page.number", is(page)))
            .andExpect(jsonPath("$.page.size", is(size)))
            .andExpect(jsonPath("$.page.totalElements", is(orders.getSize())));

        // then
        then(orderService).should().getOrdersByUserId(anyInt(), anyInt(), anyLong());
        then(orderAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - should respond with forbidden status code when any user without 'order.read' scope gets users orders")
    void getByUserId_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        long userId = 1;

        // when
        ResultActions resultActions = mockMvc.perform(
            (get("/api/orders/users/{id}", userId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "ROLE_USER")))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - should respond with forbidden status code when user gets not their orders")
    void getByUserId_shouldRespondWithForbiddenStatusCode_whenUserGetsNotTheirOrders() throws Exception {
        // given
        long userId = 1;

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/orders/users/{id}", userId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_order.read"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(userIdPermissionEvaluator).should().hasPermission(any(), anyLong(), any(), any());
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - should return orders when user gets their orders")
    void getByUserId_shouldReturnOrders_whenUserGetsTheirOrders() throws Exception {
        // given
        int page = 1;
        int size = 5;
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        Page<OrderDto> orders = new PageImpl<>(Collections.singletonList(orderDto));
        PagedModel<OrderDto> pagedOrders =
            PagedModel.of(orders.getContent(), new PagedModel.PageMetadata(size, page, orders.getSize()));

        given(orderService.getOrdersByUserId(page, size, userId)).willReturn(orders);
        given(orderAssembler.toCollectionModel(any(), any(Link.class))).willReturn(pagedOrders);
        given(userIdPermissionEvaluator.hasPermission(any(), anyLong(), any(), any())).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/orders/users/{id}", userId).with(
                jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_order.read")))
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.orderDtoList").exists())
            .andExpect(jsonPath("$.page.number", is(page)))
            .andExpect(jsonPath("$.page.size", is(size)))
            .andExpect(jsonPath("$.page.totalElements", is(orders.getSize())));

        then(userIdPermissionEvaluator).should().hasPermission(any(), anyLong(), any(), any());
        then(orderService).should().getOrdersByUserId(anyInt(), anyInt(), anyLong());
        then(orderAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/orders/users/{id} - should respond with unauthorized status code when user is not authenticated")
    void getByUserId_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        long userId = 1;

        // When / Then
        mockMvc.perform(get("/api/orders/users/{id}", userId).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isUnauthorized());

        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("POST /api/orders - should return saved order when admin user with scope 'order.write' saves order")
    void save_shouldReturnSavedOrder_whenAdminUserSavesOrder() throws Exception {
        // given
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        long certificateId = orderDto.getGiftCertificate().getId();
        MakeOrderDto makeOrderDto = new MakeOrderDto(certificateId, userId);

        given(orderService.save(makeOrderDto)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/orders").with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_order.write")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(makeOrderDto)));

        resultActions.andExpect(status().isCreated());

        // then
        then(orderService).should().save(any(OrderDto.class));
        then(orderAssembler).should().toModel(any());
    }

    @Test
    @DisplayName("POST /api/orders - should respond with forbidden status code when any user without 'order.write' scope saves order")
    void save_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        MakeOrderDto makeOrderDto = new MakeOrderDto(1, 1);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJson(makeOrderDto))
            .with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "ROLE_USER"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("POST /api/orders - should respond with forbidden status code when user saves order not on his id")
    void save_shouldRespondWithForbiddenStatusCode_whenUserSavesOrderNotOnTheirId() throws Exception {
        // given
        MakeOrderDto makeOrderDto = new MakeOrderDto(1, 1);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(asJson(makeOrderDto))
            .with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_order.read"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(userIdPermissionEvaluator).should().hasPermission(any(), anyLong(), any(), any());
        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("POST /api/orders - should return saved order when user with scope 'order.write' saves order on his id")
    void save_shouldReturnSavedOrder_whenUserSavesOrderOnTheirId() throws Exception {
        // given
        OrderDto orderDto = ModelFactory.toOrderDto(ModelFactory.createOrder());
        long userId = orderDto.getUser().getId();
        long certificateId = orderDto.getGiftCertificate().getId();
        MakeOrderDto makeOrderDto = new MakeOrderDto(certificateId, userId);

        given(userIdPermissionEvaluator.hasPermission(any(), anyLong(), any(), any())).willReturn(true);
        given(orderService.save(makeOrderDto)).willReturn(orderDto);
        given(orderAssembler.toModel(orderDto)).willReturn(orderDto);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/orders").with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_order.write")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(makeOrderDto)));

        resultActions.andExpect(status().isCreated());

        // then
        then(userIdPermissionEvaluator).should().hasPermission(any(), anyLong(), any(), any());
        then(orderService).should().save(any(OrderDto.class));
        then(orderAssembler).should().toModel(any());
    }

    @Test
    @DisplayName("POST /api/orders - should respond with unauthorized status code when user is not authenticated")
    void save_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        MakeOrderDto makeOrderDto = new MakeOrderDto(1, 1);

        // When / Then
        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE).content(asJson(makeOrderDto)))
            .andExpect(status().isUnauthorized());

        then(orderService).shouldHaveNoInteractions();
        then(orderAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("DELETE /api/orders/{id} - should delete order when admin user with scope 'order.write' deletes order")
    void delete_shouldDelete_whenAdminUserDeletesOrder() throws Exception {
        // given
        long id = 1;

        // when
        mockMvc.perform(delete("/api/orders/{id}", id).with(
                jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_order.write"))))
            .andExpect(status().isNoContent());

        // then
        then(orderService).should().delete(id);
    }

    @Test
    @DisplayName("DELETE /api/orders/{id} - should respond with forbidden status code when admin user without 'order.write' scope deletes order")
    void delete_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        long orderId = 1;

        // when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/orders/{id}", orderId).with(jwt().authorities(createAuthorityList("ROLE_ADMIN"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("DELETE /api/orders/{id} - should respond with forbidden status code when user with insufficient role deletes order")
    void delete_shouldRespondWithForbiddenStatusCode_whenUserWithInsufficientRoleDeletesOrder() throws Exception {
        // given
        long orderId = 1;

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/orders/{id}", orderId).with(
            jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_order.read"))));

        resultActions.andExpect(status().isForbidden());

        // then
        then(orderService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("DELETE /api/orders/{id} - should respond with unauthorized status code when user is not authenticated")
    void delete_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        long orderId = 1;

        // When / Then
        mockMvc.perform(delete("/api/orders/{id}", orderId)).andExpect(status().isUnauthorized());

        then(orderService).shouldHaveNoInteractions();
    }
}