package com.epam.esm.dao.querybuilder;

import com.epam.esm.dao.querybuilder.criteria.order.OrderBuilder;
import com.epam.esm.dao.querybuilder.criteria.order.impl.AscOrder;
import com.epam.esm.dao.querybuilder.criteria.order.impl.DescOrder;
import com.epam.esm.dao.querybuilder.criteria.predicate.PredicateBuilder;
import com.epam.esm.dao.querybuilder.criteria.predicate.impl.LikePredicate;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiftCertificateQueryBuilder implements QueryBuilder<GiftCertificate>  {

    private static final String FILTER_NAME = "name";
    private static final String FILTER_DESCRIPTION = "description";
    private static final String FILTER_TAGS = "tags";
    private static final String FILTER_NAME_SORT = "name_sort";
    private static final String FILTER_DATE_SORT = "date_sort";

    private static final String NAME_FIELD = "name";
    private static final String CREATE_DATE_FIELD = "createDate";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String LIKE = "like";

    private final Map<String, PredicateBuilder> predicateBuilders = Stream.of(
            new AbstractMap.SimpleEntry<String,PredicateBuilder>(LIKE, new LikePredicate())
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private final Map<String, OrderBuilder> orderBuilders = Stream.of(
            new AbstractMap.SimpleEntry<>(ASC, new AscOrder()),
            new AbstractMap.SimpleEntry<>(DESC, new DescOrder())
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    @Override
    public CriteriaQuery<GiftCertificate> buildQuery(MultiValueMap<String, String> params, CriteriaBuilder criteriaBuilder) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        List<Predicate> predicates = new LinkedList<>();
        List<Order> orders = new LinkedList<>();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String filterKey = entry.getKey();
            String filterValue = entry.getValue().stream().findFirst().orElse("");
            switch (filterKey) {
                case FILTER_NAME:
                    predicates.add(getLikePredicateBuilder().toPredicate(criteriaBuilder, root.get(NAME_FIELD), filterValue));
                    break;
                case FILTER_DESCRIPTION:
                    predicates.add(getLikePredicateBuilder().toPredicate(criteriaBuilder, root.get(DESCRIPTION_FIELD), filterValue));
                    break;
                case FILTER_TAGS:
                    List<String> tagNames = entry.getValue();
                    tagNames.forEach(
                            tagName -> predicates.add(
                                    getLikePredicateBuilder().toPredicate(
                                            criteriaBuilder, root.join(FILTER_TAGS).get(NAME_FIELD),
                                            tagName
                                    )
                            )
                    );
                    break;
                case FILTER_NAME_SORT:
                    orders.add(orderBuilders.get(filterValue).toOrder(criteriaBuilder, root.get(NAME_FIELD)));
                    break;
                case FILTER_DATE_SORT:
                    orders.add(orderBuilders.get(filterValue).toOrder(criteriaBuilder, root.get(CREATE_DATE_FIELD)));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        return criteriaQuery
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(orders);
    }

    PredicateBuilder getLikePredicateBuilder() {
        return predicateBuilders.get(LIKE);
    }
}
