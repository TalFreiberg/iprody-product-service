package com.iprody.com.productservice.dto;

import com.iprody.com.productservice.persistence.entity.ProductEntity;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class DiscountDto {
    private UUID id;
    private Integer value;
    private String summary;
    private Instant fromTime;
    private Instant untilTime;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID productId;
    private ProductEntity product;
//    private List<UUID> productsToApply;

}
