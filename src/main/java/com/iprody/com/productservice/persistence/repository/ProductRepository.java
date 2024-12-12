package com.iprody.com.productservice.persistence.repository;

import com.iprody.com.productservice.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>,
        QuerydslPredicateExecutor<ProductEntity> {

    @Query("SELECT p FROM ProductEntity p " +
            "LEFT JOIN p.discounts d " +
            "WHERE d.active = true " +
            "ORDER BY d.value DESC")
    List<ProductEntity> findAllByActiveDiscountSorted();
}
