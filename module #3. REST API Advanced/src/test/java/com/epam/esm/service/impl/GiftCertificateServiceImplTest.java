package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.converter.GiftCertificateConverter;
import com.epam.esm.exception.PersistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository certificateRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private GiftCertificateConverter giftCertificateConverter;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    private GiftCertificate GIFT_CERTIFICATE_1;
    private GiftCertificate GIFT_CERTIFICATE_2;

    private GiftCertificateDto GIFT_CERTIFICATE_DTO_1;
    private GiftCertificateDto GIFT_CERTIFICATE_DTO_2;

    private List<Tag> TAG_LIST;

    private List<TagDto> TAG_DTO_LIST;

    @BeforeEach
    void setUp() {
        TAG_LIST = Arrays.asList(
                Tag.builder().id(1L).name("tag1").build(),
                Tag.builder().id(2L).name("tag2").build(),
                Tag.builder().id(3L).name("tag3").build()
        );

        TAG_DTO_LIST = Arrays.asList(
                TagDto.builder().id(1L).name("tag1").build(),
                TagDto.builder().id(2L).name("tag2").build(),
                TagDto.builder().id(3L).name("tag3").build()
        );

        GIFT_CERTIFICATE_1 = GiftCertificate.builder()
                .id(1L).name("GiftCertificate1").description("Description")
                .price(new BigDecimal("500.00")).duration(60L)
                .createDate(LocalDateTime.parse("2022-12-15T11:43:33"))
                .lastUpdateDate((LocalDateTime.parse("2022-12-15T11:43:33")))
                .tags(TAG_LIST)
                .build();
        GIFT_CERTIFICATE_2 = GiftCertificate.builder()
                .id(2L).name("GiftCertificate2").description("Description")
                .price(new BigDecimal("200.00")).duration(1L)
                .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                .tags(TAG_LIST)
                .build();

        GIFT_CERTIFICATE_DTO_1 = GiftCertificateDto.builder()
                .id(1L).name("GiftCertificate1").description("Description")
                .price(new BigDecimal("500.00")).duration(60L)
                .createDate(LocalDateTime.parse("2022-12-15T11:43:33"))
                .lastUpdateDate((LocalDateTime.parse("2022-12-15T11:43:33")))
                .tags(TAG_DTO_LIST)
                .build();
        GIFT_CERTIFICATE_DTO_2 = GiftCertificateDto.builder()
                .id(2L).name("GiftCertificate2").description("Description")
                .price(new BigDecimal("200.00")).duration(1L)
                .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                .tags(TAG_DTO_LIST)
                .build();
    }

    @Test
    void getById_shouldReturnGiftCertificate() {
        given(certificateRepository.findById(any())).willReturn(Optional.ofNullable(GIFT_CERTIFICATE_1));
        given(giftCertificateConverter.toDto(any())).willReturn(GIFT_CERTIFICATE_DTO_1);

        GiftCertificateDto certificateById = service.getById(GIFT_CERTIFICATE_1.getId());

        assertEquals(GIFT_CERTIFICATE_DTO_1, certificateById);
    }

    @Test
    void getById_shouldThrowException() {
        long certId = GIFT_CERTIFICATE_1.getId();

        assertThrows(PersistentException.class, () -> service.getById(certId));
    }

    @Nested
    class WhenDeleting {

        @Mock
        private GiftCertificate giftCertificate;

        @Test
        void delete_shouldInvokeDelete() {
            given(certificateRepository.findById(anyLong())).willReturn(Optional.of(giftCertificate));
            willDoNothing().given(certificateRepository).delete(any());

            service.delete(1L);

            then(certificateRepository).should(times(1)).delete(any());
        }

        @Test
        void delete_shouldThrowException_whenNonexistentId() {
            assertThrows(PersistentException.class, () -> service.delete(1L));
        }
    }

    @Nested
    class WhenSaving {

        @Mock
        private GiftCertificateDto GIFT_CERTIFICATE_DTO_TO_SAVE;

        @Mock
        private GiftCertificate GIFT_CERTIFICATE_TO_SAVE;

        @BeforeEach
        void setup() {
            GIFT_CERTIFICATE_DTO_TO_SAVE = GiftCertificateDto.builder()
                    .name("GiftCertificate1").description("Description")
                    .price(new BigDecimal("500.00")).duration(60L)
                    .tags(Arrays.asList(TagDto.builder().name("tag1").build(), TagDto.builder().name("tag2").build(), TagDto.builder().name("tag3").build()))
                    .build();
            GIFT_CERTIFICATE_TO_SAVE = GiftCertificate.builder()
                    .name("GiftCertificate1").description("Description")
                    .price(new BigDecimal("500.00")).duration(60L)
                    .tags(Arrays.asList(Tag.builder().name("tag1").build(), Tag.builder().name("tag2").build(), Tag.builder().name("tag3").build()))
                    .build();
        }

        @Test
        void save_shouldReturnCertificate() {
            given(certificateRepository.findGiftCertificateByName(anyString())).willReturn(Optional.empty());
            given(giftCertificateConverter.toEntity(any())).willReturn(GIFT_CERTIFICATE_TO_SAVE);
            given(certificateRepository.save(any())).willReturn(GIFT_CERTIFICATE_1);
            given(giftCertificateConverter.toDto(any())).willReturn(GIFT_CERTIFICATE_DTO_1);

            GiftCertificateDto actual = service.save(GIFT_CERTIFICATE_DTO_TO_SAVE);

            assertEquals(GIFT_CERTIFICATE_DTO_1, actual);
        }

        @Test
        void save_shouldThrowException_whenCertificateWithNameExists() {
            given(certificateRepository.findGiftCertificateByName(anyString())).willReturn(Optional.of(GIFT_CERTIFICATE_2));

            assertThrows(PersistentException.class, () -> service.save(GIFT_CERTIFICATE_DTO_TO_SAVE));
        }
    }

    @Nested
    class WhenUpdating {

        private GiftCertificate BEFORE_UPDATE;

        private GiftCertificateDto BEFORE_UPDATE_DTO;

        private GiftCertificateDto UPDATABLE_DATA;

        private GiftCertificate AFTER_UPDATE;

        private GiftCertificateDto AFTER_UPDATE_DTO;

        @BeforeEach
        void setup() {
            BEFORE_UPDATE = GiftCertificate.builder()
                    .id(2L).name("GiftCertificate2").description("Description")
                    .price(new BigDecimal("200.00")).duration(1L)
                    .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                    .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                    .tags(TAG_LIST)
                    .build();
            BEFORE_UPDATE_DTO = GiftCertificateDto.builder()
                    .id(2L).name("GiftCertificate2").description("Description")
                    .price(new BigDecimal("200.00")).duration(1L)
                    .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                    .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                    .tags(TAG_DTO_LIST)
                    .build();
            UPDATABLE_DATA = GiftCertificateDto.builder()
                    .id(2L).name("updateName")
                    .tags(Collections.singletonList(TagDto.builder().id(1L).name("tag1").build()))
                    .build();
            AFTER_UPDATE = GiftCertificate.builder()
                    .id(2L).name("updateName").description("Description")
                    .price(new BigDecimal("200.00")).duration(1L)
                    .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                    .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                    .tags(Collections.singletonList(Tag.builder().id(1L).name("tag1").build()))
                    .build();
            AFTER_UPDATE_DTO = GiftCertificateDto.builder()
                    .id(2L).name("updateName").description("Description")
                    .price(new BigDecimal("200.00")).duration(1L)
                    .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                    .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                    .tags(Collections.singletonList(TagDto.builder().id(1L).name("tag1").build()))
                    .build();
        }

        @Test
        void update_shouldUpdate() {
            given(certificateRepository.findById(any())).willReturn(Optional.of(BEFORE_UPDATE));
            given(certificateRepository.save(any())).willReturn(AFTER_UPDATE);
            given(giftCertificateConverter.toEntity(any())).willReturn(BEFORE_UPDATE);
            given(giftCertificateConverter.toDto(any())).willReturn(AFTER_UPDATE_DTO);

            GiftCertificateDto updatedCertificate = service.update(BEFORE_UPDATE.getId(), UPDATABLE_DATA);

            assertEquals(AFTER_UPDATE_DTO, updatedCertificate);
            assertNotSame(BEFORE_UPDATE_DTO, updatedCertificate);
        }

        @Test
        void update_shouldThrowException_whenNotExistedEntity() {
            long certId = GIFT_CERTIFICATE_1.getId();

            assertThrows(PersistentException.class, () -> service.update(certId, GIFT_CERTIFICATE_DTO_1));
        }
    }

    @Nested
    class WhenGettingAllWithFilter {

        @Mock
        private MultiValueMap<String, String> params;

        @Test
        void getAllWithFilter_shouldReturnTwoGiftCertificates() {
            List<GiftCertificateDto> expectedDto = Arrays.asList(GIFT_CERTIFICATE_DTO_1, GIFT_CERTIFICATE_DTO_2);
            List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2);
            int PAGE = 37;
            int SIZE = 56;
            given(certificateRepository.findAllWithParameters(params, PageRequest.of(PAGE - 1, SIZE)))
                    .willReturn(new PageImpl<>(expected));
            given(giftCertificateConverter.toDto(any()))
                    .willReturn(GIFT_CERTIFICATE_DTO_1, GIFT_CERTIFICATE_DTO_2);

            Page<GiftCertificateDto> actual = service.getAllWithFilter(PAGE, SIZE, params);

            assertEquals(expectedDto, actual.getContent());
        }
    }

    @Nested
    class WhenUpdatingTagList {

        private Tag TAG_TO_UPDATE_1;
        private Tag TAG_TO_UPDATE_2;
        private Tag TAG_TO_UPDATE_NEW;
        private List<Tag> LIST;

        @BeforeEach
        void setup() {
            TAG_TO_UPDATE_1 = Tag.builder().id(3L).name("tag3").build();
            TAG_TO_UPDATE_2 = Tag.builder().id(4L).name("tag4").build();
            TAG_TO_UPDATE_NEW = Tag.builder().name("tag5").build();
            LIST = Arrays.asList(TAG_TO_UPDATE_1, TAG_TO_UPDATE_2, TAG_TO_UPDATE_NEW);
        }

        @Test
        void updateTagList_shouldReturnTagsWithIds() {
            given(tagRepository.findTagByName(TAG_TO_UPDATE_1.getName())).willReturn(Optional.of(TAG_TO_UPDATE_1));
            given(tagRepository.findTagByName(TAG_TO_UPDATE_2.getName())).willReturn(Optional.of(TAG_TO_UPDATE_2));
            given(tagRepository.findTagByName(TAG_TO_UPDATE_NEW.getName())).willReturn(Optional.empty());

            List<Tag> actual = service.updateTagList(LIST);
            List<Tag> empty = service.updateTagList(new ArrayList<>());

            assertTrue(empty.size() <= 0);
            assertEquals(LIST, actual);
        }
    }
}