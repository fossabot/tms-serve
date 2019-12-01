package com.odakota.tms.system.base;

import com.odakota.tms.constant.Constant;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author haidv
 * @version 1.0
 */
public class BaseSpecification<E extends BaseEntity> implements Specification<E> {

    private final String columnName;
    private final Object columnValue;
    private final String operation;

    public BaseSpecification(String columnName, Object columnValue, String operation) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        this.operation = operation;
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (Constant.OPERATION_LIKE.equals(operation)) {
            return criteriaBuilder.like(root.get(this.columnName), "%" + this.columnValue + "%");
        }
        if (Constant.OPERATION_EQUAL.equals(operation)) {
            return criteriaBuilder.and(criteriaBuilder.equal(root.<String>get(this.columnName), this.columnValue));
        }
        if (Constant.OPERATION_NOT_EQUAL.equals(operation)) {
            return criteriaBuilder.notEqual(root.<String>get(this.columnName), this.columnValue);
        }
        return null;
    }
}
