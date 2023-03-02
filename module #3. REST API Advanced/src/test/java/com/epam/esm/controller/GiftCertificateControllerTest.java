package com.epam.esm.controller;

import com.epam.esm.assembler.GiftCertificateAssembler;
import com.epam.esm.config.LanguageConfig;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.ExceptionMessageI18n;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.JsonMapperUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

@WebMvcTest({GiftCertificateController.class, ExceptionMessageI18n.class})
class GiftCertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftCertificateService giftCertificateService;

    @MockBean
    private GiftCertificateAssembler giftCertificateAssembler;


    @Test
    @DisplayName("GET /api/certificates - Success")
    void shouldReturnAllCertificatesWithParameters() throws Exception {
        GiftCertificateDto certificate1 = GiftCertificateDto.builder()
                .id(1L)
                .name("Certificate 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(10.00))
                .duration(10L)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();

        GiftCertificateDto certificate2 = GiftCertificateDto.builder()
                .id(2L)
                .name("Certificate 2")
                .description("Description 2")
                .price(BigDecimal.valueOf(20.00))
                .duration(20L)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();

        List<GiftCertificateDto> certificates = Arrays.asList(certificate1, certificate2);
        Page<GiftCertificateDto> page = new PageImpl<>(certificates);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Certificate");
        params.add("tags", "Tag1");
        params.add("name_sort", "asc");
        params.add("date_sort", "desc");

        given(giftCertificateAssembler.toCollectionModel(any(), anyInt(), anyInt(), eq(params)))
                .willReturn(PagedModel.of(certificates, new PagedModel.PageMetadata(0,0,0)));
        given(giftCertificateService.getAllWithFilter(anyInt(), anyInt(), eq(params)))
                .willReturn(page);

        mockMvc.perform(get("/api/certificates/")
                        .param("name", "Certificate")
                        .param("tags", "Tag1")
                        .param("name_sort", "asc")
                        .param("date_sort", "desc"))
                .andExpect(status().isOk());

        then(giftCertificateService).should().getAllWithFilter(anyInt(), anyInt(), eq(params));
        then(giftCertificateAssembler).should().toCollectionModel(any(), anyInt(), anyInt(), eq(params));
    }

    @Test
    @DisplayName("GET /api/certificates/{id} - Success")
    void shouldReturnCertificateById() throws Exception {
        long id = 1;
        GiftCertificateDto certificate = GiftCertificateDto.builder()
                .id(id).name("Certificate 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(10.00))
                .duration(10L)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
        given(giftCertificateService.getById(anyLong())).willReturn(certificate);
        given(giftCertificateAssembler.toModel(certificate)).willReturn(new GiftCertificateDto());

        mockMvc.perform(get("/api/certificates/" + id))
                .andExpect(status().isOk());

        then(giftCertificateService).should().getById(1L);
        then(giftCertificateAssembler).should().toModel(certificate);
    }

    @Test
    @DisplayName("POST /api/certificates - Success")
    void shouldSaveGiftCertificate() throws Exception {
        GiftCertificateDto certificate = GiftCertificateDto.builder()
                .id(1L)
                .name("Certificate 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(10.00))
                .duration(10L)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();

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
    void givenValidGiftCertificateDto_whenUpdatingGiftCertificate_thenReturnsCreatedStatus() throws Exception {
        long id = 1;
        GiftCertificateDto dto = GiftCertificateDto.builder()
                .id(id).name("Certificate 1")
                .description("Description 1")
                .price(BigDecimal.valueOf(10.00))
                .duration(10L)
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now())
                .build();
        GiftCertificateDto updatedDto = GiftCertificateDto
                .builder().id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .duration(dto.getDuration())
                .createDate(dto.getCreateDate())
                .lastUpdateDate(dto.getLastUpdateDate())
                .build();
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
    void givenExistingGiftCertificateId_whenDeletingGiftCertificate_thenReturnsNoContentStatus() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/api/certificates/" + id))
                .andExpect(status().isNoContent());

        then(giftCertificateService).should().delete(id);
    }

}