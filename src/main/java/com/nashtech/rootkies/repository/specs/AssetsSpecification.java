package com.nashtech.rootkies.repository.specs;

import com.nashtech.rootkies.model.Asset;
import com.nashtech.rootkies.model.Location;
import com.nashtech.rootkies.model.Role;
import com.nashtech.rootkies.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@AllArgsConstructor
public class AssetsSpecification implements Specification<Asset> {
    private SearchCriteria1 criteria;

    @Override
    public Predicate toPredicate(Root<Asset> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {
        if (criteria.getKey().equals("location")) {
            Join<Asset, Location> join = root.join("location");
            return builder.equal(join.<Long>get("locationId"), criteria.getValue());
        }
        if (criteria.getValue().equals("true")) {
            return builder.isTrue(root.<Boolean>get(criteria.getKey()));
        }
        if (criteria.getValue().equals("false")) {
            return builder.isFalse(root.<Boolean>get(criteria.getKey()));
        }

        if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(
                    root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }


    }
}
