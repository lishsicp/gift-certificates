package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface extends Spring's JpaRepository interface for the Tag entity, providing CRUD and basic query operations
 * for the Tag entity.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Finds a Tag by its name.
     *
     * @param name the name of the Tag to find
     * @return an Optional containing the requested Tag object, or an empty Optional if the Tag is not found
     */
    Optional<Tag> findTagByName(String name);

    /**
     * Finds the Tag that is most widely used in GiftCertificates with the highest total cost of all orders.
     * This method is implemented with a native SQL query that selects the Tag with the highest count of occurrences
     * in GiftCertificates that are included in Orders with the highest total cost of all orders.
     */
    @Query(value = "select t.id, t.name, t.created_at, t.updated_at from order_ o\n" +
            " join gift_certificate g on o.gift_certificate_id = g.id\n" +
            " join gift_certificate_tag gct on g.id = gct.gift_certificate_id\n" +
            " join tag t on gct.tag_id = t.id\n" +
            " group by t.id order by count(t.id) desc, sum(o.cost) desc limit 1", nativeQuery = true)
    Optional<Tag> findMostWidelyUsedTagWithHighestCostOfAllOrders();
}
