package com.iprody.com.productservice.dto;

import com.iprody.com.productservice.persistence.entity.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateDto {
    private String summary;
    private String description;
    private Integer price;
    private Currency currency;
    private Boolean active;
    private Integer duration;
}
