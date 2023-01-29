package com.epam.esm.entity;

import com.epam.esm.listener.AuditListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EntityListeners(AuditListener.class)
public abstract class BaseEntity<T extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    @JsonIgnore
    @Column(name = "operation", nullable = false)
    private String operation;

    @JsonIgnore
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity<?> that = (BaseEntity<?>) o;

        return new EqualsBuilder().append(id, that.id)
                .append(operation, that.operation)
                .append(timestamp, that.timestamp)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(operation)
                .append(timestamp)
                .toHashCode();
    }
}
