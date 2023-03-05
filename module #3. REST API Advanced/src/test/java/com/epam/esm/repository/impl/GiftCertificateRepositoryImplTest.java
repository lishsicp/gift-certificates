package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.extension.TestContainerExtension;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(TestContainerExtension.class)
@Transactional
@SpringBootTest
class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepository certificateRepository;

    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 5);

    @Test
    void findAllWithValidFilter_shouldReturnTwoGiftCertificates_whenValidFilterProvided() {
        // given
        List<Tag> tagList = List.of(ModelFactory.createNewTag(), ModelFactory.createNewTag());
        var certificate1 = ModelFactory.createNewGiftCertificate();
        var certificate2 = ModelFactory.createNewGiftCertificate();
        certificate1.setTags(tagList);
        certificate2.setTags(tagList);
        var savedCertificate1 = certificateRepository.save(certificate1);
        var savedCertificate2 = certificateRepository.save(certificate2);
        List<GiftCertificate> expected = Arrays.asList(savedCertificate1, savedCertificate2);

        var iter = tagList.iterator();

        MultiValueMap<String, String> filterParams = new LinkedMultiValueMap<>();
        filterParams.add("name", "gift");
        filterParams.add("description", "desc");
        filterParams.add("tags", savedCertificate1.getTags().get(tagList.indexOf(iter.next())).getName());
        filterParams.add("tags", savedCertificate1.getTags().get(tagList.indexOf(iter.next())).getName());
        filterParams.add("date_sort", "desc");
        filterParams.add("name_sort", "asc");

        // when
        Page<GiftCertificate> actual = certificateRepository.findAllWithParameters(filterParams, PAGE_REQUEST);

        // then
        assertEquals(expected, actual.getContent());
    }

    @Test
    void findAllWithInvalidFilter_shouldReturnAllGiftCertificates_whenInvalidFilterProvided() {
        // given
        var certificate1 = certificateRepository.save(ModelFactory.createNewGiftCertificate());
        var certificate2 = certificateRepository.save(ModelFactory.createNewGiftCertificate());
        var certificate3 = certificateRepository.save(ModelFactory.createNewGiftCertificate());
        MultiValueMap<String, String> filterParams = new LinkedMultiValueMap<>();
        filterParams.add("some random key", "some random value");
        List<GiftCertificate> expected = Arrays.asList(certificate1, certificate2, certificate3);

        // when
        Page<GiftCertificate> actual = certificateRepository.findAllWithParameters(filterParams, PAGE_REQUEST);

        // then
        assertEquals(expected, actual.getContent());
    }
}