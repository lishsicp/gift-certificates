package com.epam.esm.repository.querybuilder;

import com.epam.esm.repository.querybuilder.criteria.SpecificationBuilder;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GiftCertificateQueryBuilder extends SpecificationBuilder implements QueryBuilder<GiftCertificate> {

    private static final String FILTER_NAME = "name";
    private static final String FILTER_DESCRIPTION = "description";
    private static final String FILTER_TAGS = "tags";
    private static final String FILTER_NAME_SORT = "name_sort";
    private static final String FILTER_DATE_SORT = "date_sort";

    private static final String NAME_FIELD = "name";
    private static final String CREATE_DATE_FIELD = "createDate";
    private static final String DESCRIPTION_FIELD = "description";

    public GiftCertificateQueryBuilder(CriteriaBuilder criteriaBuilder) {
        super(criteriaBuilder);
    }


    /**
     * Builds a criteria query to search for gift certificates based on the provided filters and sorting parameters.
     *
     * @param params a map containing the filter and sorting parameters
     * @return a CriteriaQuery of GiftCertificate type, which can be used to execute the query
     */
    @Override
    public CriteriaQuery<GiftCertificate> buildQuery(MultiValueMap<String, String> params) {
        removeDuplicateKeys(params);
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        Predicate[] predicates = buildPredicates(params, root);
        Order[] orders = buildOrders(params, root);
        return criteriaQuery.where(predicates).orderBy(orders);
    }

    private void removeDuplicateKeys(MultiValueMap<String, String> params) {
        params.remove("size");
        params.remove("page");
    }

    /**
     * This method Builds a {@link CriteriaQuery} to count the number of gift certificates matching the given query parameters.
     *
     * @param params a {@link MultiValueMap} of query parameters to filter gift certificates
     * @return a {@link CriteriaQuery} to count the number of gift certificates matching the given query parameters
     */
    public CriteriaQuery<Long> countByQuery(MultiValueMap<String, String> params) {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        Predicate[] predicates = buildPredicates(params, root);
        if (predicates.length > 0) {
            criteriaQuery.where(criteriaBuilder.and(predicates));
        }
        return criteriaQuery;
    }

    /**
     * Builds an array of predicates based on the provided filter parameters for GiftCertificate entity.
     * Uses a stream to iterate through the filter parameters and generate predicates using the appropriate method
     * for each filter.
     *
     * @param params the map of filter parameters to be applied to the query
     * @param root   the root of the query
     * @return an array of predicates to be applied to the query
     */
    private Predicate[] buildPredicates(MultiValueMap<String, String> params, Root<GiftCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        params.forEach((filterKey, filterValues) -> {
            String filterValue = filterValues.stream().findFirst().orElse("");
            switch (filterKey) {
                case FILTER_NAME:
                    predicates.add(getLikePredicate(root.get(NAME_FIELD), filterValue));
                    break;
                case FILTER_DESCRIPTION:
                    predicates.add(getLikePredicate(root.get(DESCRIPTION_FIELD), filterValue));
                    break;
                case FILTER_TAGS:
                    filterValues.forEach(v -> predicates.add(getLikePredicate(root.join(FILTER_TAGS).get(NAME_FIELD), v)));
                    break;
                default:
                    break;
            }
        });
        return predicates.toArray(new Predicate[]{});
    }

    /**
     * Builds the orders for a GiftCertificate query based on the given parameters.
     * If the parameters contain the 'name_sort' or 'date_sort' keys, the corresponding order
     * will be added to the query.
     *
     * @param params the query parameters
     * @param root   the root entity for the query
     * @return an array of orders to apply to the query
     */
    private Order[] buildOrders(MultiValueMap<String, String> params, Root<GiftCertificate> root) {
        List<Order> orders = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String filterKey = entry.getKey();
            String filterValue = entry.getValue().stream().findFirst().orElse("");
            switch (filterKey) {
                case FILTER_NAME_SORT:
                    orders.add(getOrder(filterValue, root.get(NAME_FIELD)));
                    break;
                case FILTER_DATE_SORT:
                    orders.add(getOrder(filterValue, root.get(CREATE_DATE_FIELD)));
                    break;
                default:
                    break;
            }
        }
        return orders.toArray(new Order[]{});
    }
}
