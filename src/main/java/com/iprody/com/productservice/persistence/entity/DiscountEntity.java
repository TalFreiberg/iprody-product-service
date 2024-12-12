package com.iprody.com.productservice.persistence.entity;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@QueryEntity
@Table(name = "discount")
public class DiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "value", nullable = false)
    private int value;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "from_time", nullable = false)
    private Instant fromTime;

    @Column(name = "until_time", nullable = false)
    private Instant untilTime;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}