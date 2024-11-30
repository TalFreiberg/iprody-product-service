package service;

import com.iprody.com.productservice.service.ProductFilter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.stream.Stream;

public class ProductFilterArgProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {

        return Stream.of(
                Arguments.of("All parameters are not null, predicate = productEntity is not null "
                                + "&& productEntity.active = true",
                        ProductFilter.builder()
                                     .active(true)
                                     .directionDiscount(ASC)
                                     .directionPrice(DESC)
                                     .build(),
                        "productEntity is not null && productEntity.active = true",
                        "discounts: ASC,price: DESC",
                        "Page request [number: 0, size 25, sort: discounts: ASC,price: DESC]"),
                Arguments.of("ALL values are null, predicate = productEntity is not null",
                        ProductFilter.empty(),
                        "productEntity is not null",
                        "UNSORTED",
                        "Page request [number: 0, size 25, sort: UNSORTED]"),
                Arguments.of("All parameters are not null, predicate = productEntity is not null",
                        ProductFilter.builder()
                                     .active(true)
                                     .directionDiscount(DESC)
                                     .directionPrice(DESC)
                                     .build(),
                        "productEntity is not null && productEntity.active = true",
                        "discounts: DESC,price: DESC",
                        "Page request [number: 0, size 25, sort: discounts: DESC,price: DESC]"),
                Arguments.of("All parameters are not null, predicate = productEntity is not null",
                        ProductFilter.builder()
                                     .active(true)
                                     .directionDiscount(ASC)
                                     .directionPrice(ASC)
                                     .build(),
                        "productEntity is not null && productEntity.active = true",
                        "discounts: ASC,price: ASC",
                        "Page request [number: 0, size 25, sort: discounts: ASC,price: ASC]"),
                Arguments.of("All parameters are not null, predicate = productEntity is not null",
                        ProductFilter.builder()
                                     .active(true)
                                     .directionDiscount(DESC)
                                     .directionPrice(ASC)
                                     .build(),
                        "productEntity is not null && productEntity.active = true",
                        "discounts: DESC,price: ASC",
                        "Page request [number: 0, size 25, sort: discounts: DESC,price: ASC]")
        );
    }
}


