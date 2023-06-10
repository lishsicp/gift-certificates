package com.epam.esm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "auth_user_role")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class AuthUserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 5437138754011654816L;

    private static final String AUTH_USER_ROLE_SEQ = "auth_user_role_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = AUTH_USER_ROLE_SEQ)
    @SequenceGenerator(name = AUTH_USER_ROLE_SEQ, sequenceName = AUTH_USER_ROLE_SEQ, allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthUserRole that = (AuthUserRole) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
