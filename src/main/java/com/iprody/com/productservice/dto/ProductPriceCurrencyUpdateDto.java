package com.iprody.com.productservice.dto;

import com.iprody.com.productservice.persistence.entity.Currency;
import lombok.Data;

@Data
public class ProductPriceCurrencyUpdateDto {
    private Integer price;
    private Currency currency;
}
