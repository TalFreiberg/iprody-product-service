package com.iprody.com.productservice.dto;

import com.iprody.com.productservice.persistence.entity.Currency;
import com.iprody.com.productservice.persistence.entity.DiscountEntity;
import lombok.Data;

import java.util.List;

@Data
public class ProductCreateDto {
    private String summary;
    private String description;
    private Integer price;
    private Currency currency;
    private Integer duration;
    private List<DiscountEntity> discounts;
    private boolean active;
}



