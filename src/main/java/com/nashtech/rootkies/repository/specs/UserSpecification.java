package com.nashtech.rootkies.repository.specs;

import com.nashtech.rootkies.model.Location;
import com.nashtech.rootkies.model.Role;
import com.nashtech.rootkies.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;

@AllArgsConstructor
public class UserSpecification implements Specification<User> {
    private SearchCriteria1 criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        if (criteria.getKey().equals("role")) {
            Join<User, Role> join = root.join("role");
            return builder.equal(join.<Long>get("id"), criteria.getValue());
        }
        if (criteria.getKey().equals("location")) {
            Join<User, Location> join = root.join("location");
            return builder.equal(join.<Long>get("locationId"), criteria.getValue());
        }
        if (criteria.getValue().equals("true")) {
            return builder.isTrue(root.<Boolean>get(criteria.getKey()));
        }
        if (criteria.getValue().equals("false")) {
            return builder.isFalse(root.<Boolean>get(criteria.getKey()));
        }
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    public static Specification<User> hasIdIn(Collection<Long> userIds) {
        return (root, query, cb) -> root.<Long>get("id").in(userIds);
    }
}
