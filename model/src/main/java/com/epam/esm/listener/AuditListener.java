package com.epam.esm.listener;

import com.epam.esm.entity.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class AuditListener {

    @PrePersist
    public void onPrePersist(BaseEntity entity) {
        audit("INSERT", entity);
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity entity) {
        audit("UPDATE", entity);
    }

    private void audit(String operation, BaseEntity entity) {
        entity.setOperation(operation);
        entity.setTimestamp(Instant.now().getEpochSecond());
    }
}
