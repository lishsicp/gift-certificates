package com.epam.esm.service;

import com.epam.esm.entity.Tag;

public interface TagService extends CRDService<Tag> {
    Tag getMostWidelyUsedTagWithHighestCostOfAllOrders();
}
