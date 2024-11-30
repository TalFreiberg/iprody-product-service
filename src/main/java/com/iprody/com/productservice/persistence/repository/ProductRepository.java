package com.iprody.com.productservice.persistence.repository;

import com.iprody.com.productservice.persistence.entity.DiscountEntity;
import com.iprody.com.productservice.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>,
        QuerydslPredicateExecutor<ProductEntity> {

    @Modifying
    @Query("update product p set p.discounts=:discount where p.id=:productId")
    default void addDiscount(UUID productId,
                             DiscountEntity discount) {
    }
    @Query("SELECT * FROM products p WHERE p.id == givenId JOIN discounts d ON d.product_id = p.id" +
            "WHERE d.active == true ORDERED BY d.amount DESC")
    List<ProductEntity> findProductsSortedByActiveDiscount(UUID id);
}
