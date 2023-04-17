package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.config.TestSecurityConfig;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.JsonMapperUtil;
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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static com.epam.esm.util.JsonMapperUtil.asJson;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import({TestSecurityConfig.class})
@SpringBootTest(classes = {GiftCertificateController.class})
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@EnableMethodSecurity
class GiftCertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftCertificateService giftCertificateService;

    @MockBean
    private GiftCertificateAssembler giftCertificateAssembler;

    @Test
    @DisplayName("GET /api/certificates - Success")
    void getAllWithParameters_shouldReturnAllCertificates() throws Exception {
        // given
        GiftCertificateDto certificate1 = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        GiftCertificateDto certificate2 = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        List<GiftCertificateDto> certificates = Arrays.asList(certificate1, certificate2);
        Page<GiftCertificateDto> page = new PageImpl<>(certificates);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        var pagedModel = PagedModel.of(certificates,
            new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements()));
        given(giftCertificateAssembler.toCollectionModel(any(), any(Link.class))).willReturn(pagedModel);
        given(giftCertificateService.getAllWithFilter(anyInt(), anyInt(), eq(params))).willReturn(page);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/certificates"));

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.giftCertificateDtoList").exists())
            .andExpect(jsonPath("$.page.number", is(page.getNumber())))
            .andExpect(jsonPath("$.page.size", is(page.getSize())))
            .andExpect(jsonPath("$.page.totalElements", is((int) page.getTotalElements())));

        // when
        then(giftCertificateService).should().getAllWithFilter(anyInt(), anyInt(), eq(params));
        then(giftCertificateAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/certificates/{id} - Success")
    void getById_shouldReturnCertificate() throws Exception {
        // given
        long id = 1;
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        given(giftCertificateService.getById(anyLong())).willReturn(certificate);
        given(giftCertificateAssembler.toModel(certificate)).willReturn(new GiftCertificateDto());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/certificates/" + id));

        resultActions.andExpect(status().isOk());

        // then
        then(giftCertificateService).should().getById(id);
        then(giftCertificateAssembler).should().toModel(certificate);
    }

    @Test
    @DisplayName("POST /api/certificates - should return saved certificate when admin user with scope 'certificate.write' saves certificate")
    void save_shouldReturnSavedCertificate_whenAdminUserSavesCertificate() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());

        given(giftCertificateService.save(any(GiftCertificateDto.class))).willReturn(certificate);
        given(giftCertificateAssembler.toModel(certificate)).willReturn(certificate);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/certificates").with(
                jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_certificate.write")))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(JsonMapperUtil.asJson(certificate)));

        resultActions.andExpect(status().isCreated());

        // then
        then(giftCertificateService).should().save(any(GiftCertificateDto.class));
        then(giftCertificateAssembler).should().toModel(certificate);
    }

    @Test
    @DisplayName("POST /api/certificates - should respond with forbidden status code when admin user without 'certificate.write' scope saves certificate")
    void save_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/certificates").with(jwt().authorities(createAuthorityList("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(certificate)));

        // then
        resultActions.andExpect(status().isForbidden());

        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("POST /api/certificates - should respond with forbidden status code when user with insufficient role saves certificate")
    void save_shouldRespondWithForbiddenStatusCode_whenInsufficientRole() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/certificates").with(jwt().authorities(createAuthorityList("ROLE_USER")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(certificate)));

        // then
        resultActions.andExpect(status().isForbidden());

        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("POST /api/certificates - should respond with unauthorized status code when user is not authenticated")
    void save_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/api/certificates").contentType(MediaType.APPLICATION_JSON_VALUE).content(asJson(certificate)));

        // then
        resultActions.andExpect(status().isUnauthorized());

        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("PATCH /api/certificates/{id} - should return updated certificate when admin with scope 'certificate.write' updates certificate")
    void update_shouldReturnUpdatedCertificate_whenAdminUserUpdatesCertificate() throws Exception {
        // given
        var giftCertificate = ModelFactory.createGiftCertificate();
        long id = giftCertificate.getId();
        GiftCertificateDto dto = ModelFactory.toGiftCertificateDto(giftCertificate);
        GiftCertificateDto updatedDto = ModelFactory.toGiftCertificateDto(giftCertificate);
        given(giftCertificateService.update(anyLong(), any(GiftCertificateDto.class))).willReturn(updatedDto);
        given(giftCertificateAssembler.toModel(any())).willReturn(updatedDto);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/certificates/{id}", id).with(
                jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_certificate.write")))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(JsonMapperUtil.asJson(dto)));

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(dto.getName())))
            .andExpect(jsonPath("$.description", is(dto.getDescription())))
            .andExpect(jsonPath("$.price", is(dto.getPrice().doubleValue())))
            .andExpect(jsonPath("$.duration", is((int) dto.getDuration())));

        // given
        then(giftCertificateService).should().update(anyLong(), any(GiftCertificateDto.class));
    }

    @Test
    @DisplayName("PATCH /api/certificates/{id} - should respond with forbidden status code when admin without 'certificate.write' scope updates certificate")
    void update_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        long id = certificate.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/certificates/{id}", id).with(jwt().authorities(createAuthorityList("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(certificate)));

        // then
        resultActions.andExpect(status().isForbidden());

        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("PATCH /api/certificates/{id} - should respond with forbidden status code when user with insufficient role saves certificate")
    void update_shouldRespondWithForbiddenStatusCode_whenInsufficientRole() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        long id = certificate.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/certificates/{id}", id).with(jwt().authorities(createAuthorityList("ROLE_USER")))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(certificate)));

        // then
        resultActions.andExpect(status().isForbidden());

        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("PATCH /api/certificates/{id} - should respond with unauthorized status code when user is not authenticated")
    void update_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        long id = certificate.getId();

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/api/certificates/{id}", id).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(certificate)));

        // then
        resultActions.andExpect(status().isUnauthorized());

        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("DELETE /api/certificates/{id} - should delete certificate when admin with scope 'certificate.write' deletes certificate")
    void deleteById_shouldDeleteGiftCertificate_whenAdminUserDeletesCertificate() throws Exception {
        // given
        long id = 1L;

        // when
        mockMvc.perform(delete("/api/certificates//{id}", id).with(
                jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_certificate.write"))))
            .andExpect(status().isNoContent());

        // then
        then(giftCertificateService).should().delete(id);
    }

    @Test
    @DisplayName("DELETE /api/certificates/{id} - should respond with forbidden status code when user with insufficient role deletes certificate")
    void deleteById_shouldRespondWithForbiddenStatusCode_whenInsufficientRole() throws Exception {
        // given
        long id = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/certificates/{id}", id).with(
            jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_certificate.write"))));

        // then
        resultActions.andExpect(status().isForbidden());
        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("DELETE /api/certificates/{id} - should respond with forbidden status code when admin user without 'certificate.write' scope deletes certificate")
    void deleteById_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        long id = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(
            delete("/api/certificates/" + id).with(jwt().authorities(createAuthorityList("ROLE_USER"))));

        // then
        resultActions.andExpect(status().isForbidden());
        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("DELETE /api/certificates/{id} - should respond with unauthorized status code when user is not authenticated")
    void deleteById_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        long id = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/certificates/{id}", id));

        // then
        resultActions.andExpect(status().isUnauthorized());

        then(giftCertificateService).shouldHaveNoInteractions();
        then(giftCertificateAssembler).shouldHaveNoInteractions();
    }


}