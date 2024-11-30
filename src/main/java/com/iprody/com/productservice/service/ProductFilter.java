package com.iprody.com.productservice.service;

import static com.iprody.com.productservice.persistence.entity.QProductEntity.productEntity;
import static com.querydsl.core.types.ExpressionUtils.and;

import com.querydsl.core.types.Predicate;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import static org.springframework.data.domain.Sort.unsorted;

@Builder
public record ProductFilter(String summary,
                            Boolean active,
                            Direction directionDiscount,
                            Direction directionPrice,
                            int pageNumber,
                            int pageSize) {

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    public static class ProductFilterBuilder {
        ProductFilterBuilder() {
            pageNumber = DEFAULT_PAGE_NUMBER;
            pageSize = DEFAULT_PAGE_SIZE;
        }
    }

    public static ProductFilter empty() {
        return ProductFilter.builder()
                            .build();
    }

    public Predicate createPredicate() {
        Predicate predicate = productEntity.isNotNull();
        if (null != summary) {
            predicate = and(predicate, productEntity.summary.like(summary));
        }
        if (null != active) {
            predicate = and(predicate, productEntity.active.eq(active));
        }
        return predicate;
    }

    public Pageable createPageable() {
        return PageRequest.of(pageNumber, pageSize, createSort());
    }

    public Sort createSort() {
        Sort sortOrder = unsorted();

        if (null != directionDiscount) {
            sortOrder = sortOrder.and(Sort.by(directionDiscount,
                    productEntity.discounts.getMetadata()
                                           .getName()));
        } //TODO сделать сортировку по активной скидке

        if (null != directionPrice) {
            sortOrder = sortOrder.and(Sort.by(directionPrice,
                    productEntity.price.getMetadata()
                                       .getName()));
        }
        return sortOrder;
    }
}
