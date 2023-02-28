package com.epam.esm.service.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.converter.GiftCertificateConverter;
import com.epam.esm.service.exception.ErrorCodes;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ZoneId zoneId = ZoneId.systemDefault();

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateConverter giftCertificateConverter;

    @Autowired
    public GiftCertificateServiceImpl(
            GiftCertificateRepository giftCertificateRepository,
            TagRepository tagRepository,
            GiftCertificateConverter giftCertificateConverter) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.giftCertificateConverter = giftCertificateConverter;
    }

    @Override
    public Page<GiftCertificateDto> getAll(int page, int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<GiftCertificateDto> getAllWithFilter(int page, int size, MultiValueMap<String, String> params) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<GiftCertificate> allWithParameters = giftCertificateRepository
                .findAllWithParameters(params, pageable);
        return allWithParameters.map(giftCertificateConverter::toDto);
    }

    @Override
    public GiftCertificateDto getById(long id) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        }
        return giftCertificateConverter.toDto(optionalGiftCertificate.get());
    }

    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) {
        Optional<GiftCertificate> existed = giftCertificateRepository.findGiftCertificateByName(giftCertificateDto.getName());
        if (existed.isPresent())
            throw new PersistentException(ErrorCodes.DUPLICATED_CERTIFICATE, giftCertificateDto.getName());
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        GiftCertificate giftCertificate = giftCertificateConverter.toEntity(giftCertificateDto);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);
        giftCertificate.setTags(updateTagList(giftCertificate.getTags()));
        GiftCertificate savedCertificate = giftCertificateRepository.save(giftCertificate);
        return giftCertificateConverter.toDto(savedCertificate);
    }

    @Override
    public void delete(long id) throws PersistentException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateRepository.findById(id);
        if (giftCertificateOptional.isEmpty())
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        giftCertificateRepository.delete(giftCertificateOptional.get());
    }

    @Override
    public GiftCertificateDto update(long id, GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificateToUpdate = giftCertificateRepository
                .findById(id)
                .orElseThrow(() -> new PersistentException(ErrorCodes.CERTIFICATE_NOT_FOUND, id));
        GiftCertificate updateData = giftCertificateConverter.toEntity(giftCertificateDto);
        Optional.ofNullable(updateData.getName()).ifPresent(giftCertificateToUpdate::setName);
        Optional.ofNullable(updateData.getDescription()).ifPresent(giftCertificateToUpdate::setDescription);
        Optional.ofNullable(updateData.getPrice()).ifPresent(giftCertificateToUpdate::setPrice);
        Optional.ofNullable(updateData.getDuration()).ifPresent(giftCertificateToUpdate::setDuration);
        if (giftCertificateDto.getTags() != null) {
            giftCertificateToUpdate.setTags(updateTagList(updateData.getTags()));
        }
        giftCertificateToUpdate.setLastUpdateDate(LocalDateTime.now(zoneId));
        return giftCertificateConverter.toDto(giftCertificateRepository.save(giftCertificateToUpdate));
    }

    public List<Tag> updateTagList(List<Tag> list) {
        if (list == null || list.isEmpty())
            return list;
        List<Tag> tagList = new ArrayList<>();
        for (Tag tagFromRequest : list) {
            Optional<Tag> tagFromDb = tagRepository.findTagByName(tagFromRequest.getName());
            if (tagFromDb.isPresent()) {
                tagList.add(tagFromDb.get());
            } else {
                tagList.add(tagFromRequest);
            }
        }
        return tagList;
    }
}
