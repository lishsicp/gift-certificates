package com.epam.esm.dao.querybuilder;

import com.epam.esm.dao.querybuilder.criteria.SpecificationBuilder;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.*;
import java.util.*;

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

    @Override
    public CriteriaQuery<GiftCertificate> buildQuery(MultiValueMap<String, String> params) {
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        List<Predicate> predicates = new LinkedList<>();
        List<Order> orders = new LinkedList<>();

        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String filterKey = entry.getKey();
            String filterValue = entry.getValue().stream().findFirst().orElse("");
            switch (filterKey) {
                case FILTER_NAME:
                    predicates.add(getLikePredicate(root.get(NAME_FIELD), filterValue));
                    break;
                case FILTER_DESCRIPTION:
                    predicates.add(getLikePredicate(root.get(DESCRIPTION_FIELD), filterValue));
                    break;
                case FILTER_TAGS:
                    List<String> tagNames = entry.getValue();
                    tagNames.forEach(
                            tagName -> predicates.add(
                                    getLikePredicate(root.join(FILTER_TAGS).get(NAME_FIELD), tagName)
                            )
                    );
                    break;
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
        return criteriaQuery
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(orders);
    }
}
