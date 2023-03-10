package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.converter.GiftCertificateConverter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.ModelFactory;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.anyString;

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

    @Test
    void getById_shouldReturnGiftCertificate() {
        var giftCertificate = ModelFactory.createGiftCertificate();
        var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);
        given(certificateRepository.findById(any())).willReturn(Optional.ofNullable(giftCertificate));
        given(giftCertificateConverter.toDto(any())).willReturn(giftCertificateDto);

        GiftCertificateDto certificateById = service.getById(giftCertificateDto.getId());

        assertEquals(giftCertificateDto, certificateById);
    }

    @Test
    void getById_shouldThrowException() {
        assertThrows(PersistentException.class, () -> service.getById(0));
    }

    @Nested
    class WhenDeleting {

        @Test
        void delete_shouldInvokeDelete() {
            var giftCertificate = ModelFactory.createGiftCertificate();
            given(certificateRepository.findById(anyLong())).willReturn(Optional.of(giftCertificate));
            //willDoNothing().given(certificateRepository).delete(any());

            service.delete(1L);

            then(certificateRepository).should().delete(any());
        }

        @Test
        void delete_shouldThrowException_whenNonexistentId() {
            assertThrows(PersistentException.class, () -> service.delete(0));
        }
    }

    @Nested
    class WhenSaving {

        @Test
        void save_shouldReturnCertificate() {
            var giftCertificate = ModelFactory.createGiftCertificate();
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);
            given(certificateRepository.findGiftCertificateByName(anyString())).willReturn(Optional.empty());
            given(giftCertificateConverter.toEntity(any())).willReturn(giftCertificate);
            given(certificateRepository.save(any())).willReturn(giftCertificate);
            given(giftCertificateConverter.toDto(any())).willReturn(giftCertificateDto);

            GiftCertificateDto actual = service.save(giftCertificateDto);

            assertEquals(giftCertificateDto, actual);
        }

        @Test
        void save_shouldThrowException_whenCertificateWithNameExists() {
            var giftCertificate = ModelFactory.createGiftCertificate();
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);
            given(certificateRepository.findGiftCertificateByName(anyString())).willReturn(Optional.of(giftCertificate));

            assertThrows(PersistentException.class, () -> service.save(giftCertificateDto));
        }
    }

    @Nested
    class WhenUpdating {

        @Test
        void update_shouldUpdate() {
            var giftCertificate = ModelFactory.createGiftCertificate();
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);
            given(certificateRepository.findById(any())).willReturn(Optional.of(giftCertificate));
            given(certificateRepository.save(any())).willReturn(giftCertificate);
            given(giftCertificateConverter.toEntity(any())).willReturn(giftCertificate);
            given(giftCertificateConverter.toDto(any())).willReturn(giftCertificateDto);

            GiftCertificateDto updatedCertificate = service.update(giftCertificate.getId(), giftCertificateDto);

            assertEquals(giftCertificateDto, updatedCertificate);
        }

        @Test
        void update_shouldThrowException_whenNotExistedEntity() {
            var giftCertificate = ModelFactory.createGiftCertificate();
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);

            assertThrows(PersistentException.class, () -> service.update(0, giftCertificateDto));
        }
    }

    @Nested
    class WhenGettingAllWithFilter {

        @Mock
        MultiValueMap<String, String> params;

        @Test
        void getAllWithFilter_shouldReturnTwoGiftCertificates() {
            var giftCertificate = ModelFactory.createGiftCertificate();
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);
            List<GiftCertificateDto> expectedDto = Collections.singletonList(giftCertificateDto);
            List<GiftCertificate> expected = Collections.singletonList(giftCertificate);
            int PAGE = 37;
            int SIZE = 56;

            given(certificateRepository.findAllWithParameters(params, PageRequest.of(PAGE - 1, SIZE)))
                    .willReturn(new PageImpl<>(expected));
            given(giftCertificateConverter.toDto(any()))
                    .willReturn(giftCertificateDto);

            Page<GiftCertificateDto> actual = service.getAllWithFilter(PAGE, SIZE, params);

            assertEquals(expectedDto, actual.getContent());
        }
    }

    @Nested
    class WhenUpdatingTagList {

        @Test
        void updateTagList_shouldReturnTagsWithIds() {
            var tag1 = ModelFactory.createTag();
            var tag2 = ModelFactory.createTag();
            var newTag = ModelFactory.createNewTag();
            List<Tag> tagList = List.of(tag1, tag2, newTag);
            given(tagRepository.findTagByName(tag1.getName())).willReturn(Optional.of(tag1));
            given(tagRepository.findTagByName(tag2.getName())).willReturn(Optional.of(tag2));
            given(tagRepository.findTagByName(newTag.getName())).willReturn(Optional.empty());

            List<Tag> actual = service.updateTagList(tagList);

            assertEquals(tagList, actual);
        }
    }
}