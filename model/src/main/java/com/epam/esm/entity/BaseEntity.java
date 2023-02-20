package com.epam.esm.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.MappedSuperclass;
import javax.persistence.EntityListeners;
import javax.persistence.Column;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "created_at")
    private String createdDate;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Long lastModifiedDate;
}
