package com.epam.esm.service;

import com.epam.esm.service.dto.TagDto;

public interface TagService extends CRDService<TagDto> {
    TagDto getMostWidelyUsedTagWithHighestCostOfAllOrders();
}
