package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = GiftCertificateController.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
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
        var pagedModel = PagedModel
            .of(certificates, new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements()));
        given(giftCertificateAssembler.toCollectionModel(any(), any(Link.class))).willReturn(pagedModel);
        given(giftCertificateService.getAllWithFilter(anyInt(), anyInt(), eq(params)))
            .willReturn(page);

        // when
        ResultActions resultActions = mockMvc
            .perform(get("/api/certificates/"));

        resultActions
            .andExpect(status().isOk())
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
        ResultActions resultActions = mockMvc
            .perform(get("/api/certificates/" + id));

        resultActions
            .andExpect(status().isOk());

        // then
        then(giftCertificateService).should().getById(id);
        then(giftCertificateAssembler).should().toModel(certificate);
    }

    @Test
    @DisplayName("POST /api/certificates - Success")
    void save_shouldSaveGiftCertificate_thenReturnsCreatedStatus() throws Exception {
        // given
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());

        given(giftCertificateService.save(any(GiftCertificateDto.class))).willReturn(certificate);
        given(giftCertificateAssembler.toModel(certificate)).willReturn(certificate);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/certificates/")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(JsonMapperUtil.asJson(certificate)));

        resultActions
            .andExpect(status().isCreated());

        // then
        then(giftCertificateService).should().save(any(GiftCertificateDto.class));
        then(giftCertificateAssembler).should().toModel(certificate);
    }

    @Test
    @DisplayName("PATCH /api/certificates/{id} - Success")
    void update_shouldUpdate_thenReturnsCreatedStatus() throws Exception {
        // given
        var giftCertificate = ModelFactory.createGiftCertificate();
        long id = giftCertificate.getId();
        GiftCertificateDto dto = ModelFactory.toGiftCertificateDto(giftCertificate);
        GiftCertificateDto updatedDto = ModelFactory.toGiftCertificateDto(giftCertificate);
        given(giftCertificateService.update(anyLong(), any(GiftCertificateDto.class))).willReturn(updatedDto);
        given(giftCertificateAssembler.toModel(any())).willReturn(updatedDto);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/certificates/" + id)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(JsonMapperUtil.asJson(dto)));

        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(dto.getName())))
            .andExpect(jsonPath("$.description", is(dto.getDescription())))
            .andExpect(jsonPath("$.price", is(dto.getPrice().doubleValue())))
            .andExpect(jsonPath("$.duration", is((int) dto.getDuration())));

        // given
        then(giftCertificateService).should().update(anyLong(), any(GiftCertificateDto.class));
    }

    @Test
    @DisplayName("DELETE /api/certificates/{id} - Success")
    void deleteById_shouldDeleteGiftCertificate() throws Exception {
        // given
        long id = 1L;

        // when
        mockMvc.perform(delete("/api/certificates/" + id))
            .andExpect(status().isNoContent());

        // then
        then(giftCertificateService).should().delete(id);
    }

}