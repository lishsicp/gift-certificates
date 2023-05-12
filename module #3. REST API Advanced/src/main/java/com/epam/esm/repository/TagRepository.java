package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Finds a {@link Tag} by its name.
     *
     * @param name the name of the {@link Tag} to find
     * @return an {@link Optional Optional} containing the requested {@link Tag} object
     */
    Optional<Tag> findTagByName(String name);

    /**
     * Finds the {@link Tag tag} that is most widely used in {@link com.epam.esm.entity.GiftCertificate GiftCertificate}
     * with the highest total cost of all {@link com.epam.esm.entity.Order orders}.
     */
    @Query(value = "select t.id, t.name, t.created_at, t.updated_at from order_ o\n" +
        " join gift_certificate g on o.gift_certificate_id = g.id\n" +
        " join gift_certificate_tag gct on g.id = gct.gift_certificate_id\n" +
        " join tag t on gct.tag_id = t.id\n" +
        " group by t.id order by count(t.id) desc, sum(o.price) desc limit 1", nativeQuery = true)
    Optional<Tag> findMostWidelyUsedTagWithHighestCostOfAllOrders();
}
