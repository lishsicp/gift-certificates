package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.extension.TestContainerExtension;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestContainerExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class GiftCertificateServiceImplIT {

    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private GiftCertificateService giftCertificateService;

    @Test
    void getById_shouldReturnGiftCertificate() {
        var giftCertificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());
        var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);

        GiftCertificateDto certificateById = giftCertificateService.getById(giftCertificate.getId());

        assertEquals(giftCertificateDto, certificateById);
    }

    @Test
    void getById_shouldThrowException() {
        assertThrows(PersistentException.class, () -> giftCertificateService.getById(0));
    }

    @Nested
    class WhenDeleting {

        @Test
        void delete_shouldReturnEmptyOptional_whenWasDeleted() {
            var giftCertificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());

            giftCertificateService.delete(giftCertificate.getId());

            assertTrue(certificateRepository.findById(giftCertificate.getId()).isEmpty());
        }

        @Test
        void delete_shouldThrowException_whenNonexistentId() {
            assertThrows(PersistentException.class, () -> giftCertificateService.delete(0));
        }
    }

    @Nested
    class WhenSaving {

        @Test
        void save_shouldReturnCertificate() {
            var newGiftCertificate = ModelFactory.createNewGiftCertificate();
            var newGiftCertificateDto = ModelFactory.toGiftCertificateDto(newGiftCertificate);

            GiftCertificateDto actual = giftCertificateService.save(newGiftCertificateDto);

            assertTrue(actual.getId() > 0);
            assertEquals(newGiftCertificateDto.getName(), actual.getName());
            assertEquals(newGiftCertificateDto.getDescription(), actual.getDescription());
            assertEquals(newGiftCertificateDto.getPrice(), actual.getPrice());
            assertEquals(newGiftCertificateDto.getDuration(), actual.getDuration());
            assertNotSame(newGiftCertificateDto.getCreateDate(), actual.getCreateDate());
            assertNotSame(newGiftCertificateDto.getLastUpdateDate(), actual.getLastUpdateDate());
        }

        @Test
        void save_shouldThrowException_whenCertificateWithNameExists() {
            var giftCertificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);
            assertThrows(PersistentException.class, () -> giftCertificateService.save(giftCertificateDto));
        }
    }

    @Nested
    class WhenUpdating {

        @Test
        void update_shouldUpdate() {
            var giftCertificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);
            var updatableData =  ModelFactory.toGiftCertificateDto(ModelFactory.createGiftCertificate());

            GiftCertificateDto updatedCertificate = giftCertificateService.update(giftCertificateDto.getId(), updatableData);

            updatableData.setId(giftCertificateDto.getId());
            updatableData.setLastUpdateDate(updatedCertificate.getLastUpdateDate());
            assertEquals(updatableData, updatedCertificate);
        }

        @Test
        void update_shouldThrowException_whenNotExistedEntity() {
            var giftCertificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());
            var giftCertificateDto = ModelFactory.toGiftCertificateDto(giftCertificate);

            assertThrows(PersistentException.class, () -> giftCertificateService.update(0, giftCertificateDto));
        }
    }

    @Nested
    class WhenGettingAllWithFilter {

        @Test
        void getAllWithFilter_shouldReturnOneGiftCertificate() {
            // given
            var tag1 = tagRepository.save(ModelFactory.createNewTag());
            var giftCertificate1 = ModelFactory.createNewGiftCertificate();
            giftCertificate1.setTags(Collections.singletonList(tag1));
            var giftCertificateDto1 = ModelFactory.toGiftCertificateDto(certificateRepository.save(giftCertificate1));
            certificateRepository.save(ModelFactory.createNewGiftCertificate());

            List<GiftCertificateDto> expectedDto = Collections.singletonList(giftCertificateDto1);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("name", giftCertificate1.getName());
            params.add("description", giftCertificate1.getDescription());
            params.add("name_sort", "asc");
            params.add("tags", tag1.getName());

            // when
            Page<GiftCertificateDto> actual = giftCertificateService.getAllWithFilter(1, 1, params);

            // then
            assertEquals(expectedDto, actual.getContent());
        }
    }
}