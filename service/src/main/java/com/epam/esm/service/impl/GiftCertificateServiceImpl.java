package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.converter.GiftCertificateConverter;
import com.epam.esm.service.exception.ExceptionErrorCode;
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

    private final GiftCertificateRepository giftCertificateDao;
    private final TagRepository tagDao;
    private final GiftCertificateConverter giftCertificateConverter;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateDao, TagRepository tagDao, GiftCertificateConverter giftCertificateConverter) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateConverter = giftCertificateConverter;
    }

    @Override
    public Page<GiftCertificateDto> getAll(int page, int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<GiftCertificateDto> getAllWithFilter(int page, int size, MultiValueMap<String, String> params) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<GiftCertificate> allWithParameters = giftCertificateDao.findAllWithParameters(params, pageable);
        return allWithParameters.map(giftCertificateConverter::toDto);
    }

    @Override
    public GiftCertificateDto getById(Long id) throws PersistentException {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isEmpty()) {
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id);
        }
        return giftCertificateConverter.toDto(optionalGiftCertificate.get());
    }

    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) throws PersistentException {
        Optional<GiftCertificate> existed = giftCertificateDao.findGiftCertificateByName(giftCertificateDto.getName());
        if (existed.isPresent())
            throw new PersistentException(ExceptionErrorCode.DUPLICATED_CERTIFICATE, giftCertificateDto.getName());
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        GiftCertificate giftCertificate = giftCertificateConverter.toEntity(giftCertificateDto);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);
        giftCertificate.setTags(updateTagList(giftCertificate.getTags()));
        GiftCertificate savedCertificate = giftCertificateDao.save(giftCertificate);
        return giftCertificateConverter.toDto(savedCertificate);
    }

    @Override
    public void delete(Long id) throws PersistentException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(id);
        if (giftCertificateOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id);
        giftCertificateDao.delete(giftCertificateOptional.get());
    }

    @Override
    public GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto) throws PersistentException {
        GiftCertificate giftCertificateToUpdate = giftCertificateDao
                .findById(id)
                .orElseThrow(() -> new PersistentException(ExceptionErrorCode.CERTIFICATE_NOT_FOUND));
        GiftCertificate updateData = giftCertificateConverter.toEntity(giftCertificateDto);
        Optional.ofNullable(updateData.getName()).ifPresent(giftCertificateToUpdate::setName);
        Optional.ofNullable(updateData.getDescription()).ifPresent(giftCertificateToUpdate::setDescription);
        Optional.ofNullable(updateData.getPrice()).ifPresent(giftCertificateToUpdate::setPrice);
        Optional.ofNullable(updateData.getDuration()).ifPresent(giftCertificateToUpdate::setDuration);
        if (giftCertificateDto.getTags() != null) {
            giftCertificateToUpdate.setTags(updateTagList(updateData.getTags()));
        }
        giftCertificateToUpdate.setLastUpdateDate(LocalDateTime.now(zoneId));
        return giftCertificateConverter.toDto(giftCertificateDao.save(giftCertificateToUpdate));
    }

    public List<Tag> updateTagList(List<Tag> list) {
        if (list == null || list.isEmpty())
            return list;
        List<Tag> tagList = new ArrayList<>();
        for (Tag tagFromRequest : list) {
            Optional<Tag> tagFromDb = tagDao.findTagByName(tagFromRequest.getName());
            if (tagFromDb.isPresent()) {
                tagList.add(tagFromDb.get());
            } else {
                tagList.add(tagFromRequest);
            }
        }
        return tagList;
    }
}
