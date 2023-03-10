package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.JsonMapperUtil;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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


@ExtendWith(MockitoExtension.class)
class GiftCertificateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GiftCertificateService giftCertificateService;

    @Mock
    private GiftCertificateAssembler giftCertificateAssembler;

    @InjectMocks
    private GiftCertificateController giftCertificateController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(giftCertificateController).build();
    }

    @Test
    @DisplayName("GET /api/certificates - Success")
    void findAllCertificatesWithParameters_shouldReturnAllCertificates() throws Exception {
        GiftCertificateDto certificate1 = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        GiftCertificateDto certificate2 = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        List<GiftCertificateDto> certificates = Arrays.asList(certificate1, certificate2);
        Page<GiftCertificateDto> page = new PageImpl<>(certificates);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        given(giftCertificateAssembler.toCollectionModel(any(), any(Link.class)))
                .willReturn(PagedModel.of(certificates, new PagedModel.PageMetadata(0,0,0)));
        given(giftCertificateService.getAllWithFilter(anyInt(), anyInt(), eq(params)))
                .willReturn(page);

        mockMvc.perform(get("/api/certificates/"))
                .andExpect(status().isOk());

        then(giftCertificateService).should().getAllWithFilter(anyInt(), anyInt(), eq(params));
        then(giftCertificateAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/certificates/{id} - Success")
    void giftCertificateById_shouldReturnCertificate() throws Exception {
        long id = 1;
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());
        given(giftCertificateService.getById(anyLong())).willReturn(certificate);
        given(giftCertificateAssembler.toModel(certificate)).willReturn(new GiftCertificateDto());

        mockMvc.perform(get("/api/certificates/" + id))
                .andExpect(status().isOk());

        then(giftCertificateService).should().getById(1L);
        then(giftCertificateAssembler).should().toModel(certificate);
    }

    @Test
    @DisplayName("POST /api/certificates - Success")
    void saveGiftCertificate_shouldSaveGiftCertificate_thenReturnsCreatedStatus() throws Exception {
        GiftCertificateDto certificate = ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());

        given(giftCertificateService.save(any(GiftCertificateDto.class))).willReturn(certificate);
        given(giftCertificateAssembler.toModel(certificate)).willReturn(certificate);

        mockMvc.perform(post("/api/certificates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapperUtil.asJson(certificate)))
                .andExpect(status().isCreated());

        then(giftCertificateService).should().save(any(GiftCertificateDto.class));
        then(giftCertificateAssembler).should().toModel(certificate);
    }

    @Test
    @DisplayName("PATCH /api/certificates/{id} - Success")
    void updateGiftCertificate_shouldUpdate_thenReturnsCreatedStatus() throws Exception {
        var giftCertificate = ModelFactory.createGiftCertificate();
        long id = giftCertificate.getId();
        GiftCertificateDto dto = ModelFactory.toGiftCertificateDto(giftCertificate);
        GiftCertificateDto updatedDto = ModelFactory.toGiftCertificateDto(giftCertificate);
        given(giftCertificateService.update(id, dto)).willReturn(updatedDto);
        given(giftCertificateAssembler.toModel(any())).willReturn(updatedDto);

        String json = JsonMapperUtil.asJson(dto);
        ResultActions resultActions = mockMvc.perform(patch("/api/certificates/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.description", is(dto.getDescription())))
                .andExpect(jsonPath("$.price", is(dto.getPrice().doubleValue())))
                .andExpect(jsonPath("$.duration", is((dto.getDuration().intValue()))));

        then(giftCertificateService).should().update(id, dto);
    }

    @Test
    @DisplayName("DELETE /api/certificates/{id} - Success")
    void deleteGiftCertificate_shouldDeleteGiftCertificate() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/api/certificates/" + id))
                .andExpect(status().isNoContent());

        then(giftCertificateService).should().delete(id);
    }

}