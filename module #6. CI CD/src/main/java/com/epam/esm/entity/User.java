package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "user_")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    public static final String USER_SEQ = "user_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_SEQ)
    @SequenceGenerator(name = USER_SEQ, sequenceName = USER_SEQ, allocationSize = 1)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
