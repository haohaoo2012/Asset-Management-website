package com.nashtech.rootkies.repository.specs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.nashtech.rootkies.model.Asset;
import com.nashtech.rootkies.model.Assignment;
import com.nashtech.rootkies.model.Location;
import com.nashtech.rootkies.model.User;

import org.springframework.data.jpa.domain.Specification;

public class AssignmentSpecification implements Specification<Assignment> {

    private List<SearchCriteria> list;

    public AssignmentSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Assignment> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        // add add criteria to predicates
        for (SearchCriteria criteria : list) {
            if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
                predicates
                        .add(builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
                predicates.add(builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
                predicates.add(builder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
                if (criteria.getKey().equals("assignedBy")) {
                    Join<Assignment, User> groupJoin = root.join("assignedBy");
                    Join<User, Location> groupJoin2 = groupJoin.join("location");
                    predicates.add(builder.equal(groupJoin2.get("locationId"), criteria.getValue()));
                } else {
                    predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
                }
            } else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
                if (criteria.getKey().equals("assetCode")) {
                    Join<Assignment, Asset> groupJoin = root.join("asset");
                    predicates.add(builder.like(builder.lower(groupJoin.<String>get("assetCode")),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                } else if (criteria.getKey().equals("assetName")) {
                    Join<Assignment, Asset> groupJoin = root.join("asset");
                    predicates.add(builder.like(builder.lower(groupJoin.<String>get("assetName")),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                } else if (criteria.getKey().equals("assignedTo")) {
                    Join<Assignment, User> groupJoin = root.join("assignedTo");
                    predicates.add(builder.like(builder.lower(groupJoin.<String>get("username")),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                } else {
                    predicates.add(builder.like(builder.lower(root.get(criteria.getKey())),
                            "%" + criteria.getValue().toString().toLowerCase() + "%"));
                }
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())),
                        criteria.getValue().toString().toLowerCase() + "%"));
            } else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
                predicates.add(builder.like(builder.lower(root.get(criteria.getKey())),
                        "%" + criteria.getValue().toString().toLowerCase()));
            } else if (criteria.getOperation().equals(SearchOperation.IN)) {
                predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
            } else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
                predicates.add(builder.not(root.get(criteria.getKey())).in(criteria.getValue()));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
