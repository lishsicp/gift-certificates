package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at")
    private String createdDate;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "updated_at")
    private Long lastModifiedDate;
}
