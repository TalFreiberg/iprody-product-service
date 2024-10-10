package com.iprody.com.productservice.persistence.repository;

import com.iprody.com.productservice.persistence.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, UUID>,
                                            QuerydslPredicateExecutor<DiscountEntity> {
}
