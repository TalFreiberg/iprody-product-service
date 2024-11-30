package service;

import com.iprody.com.productservice.service.ProductFilter;
import com.querydsl.core.types.Predicate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductFilterTest {

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ProductFilterArgProvider.class)
    @DisplayName("CreatePredicate, createSort, and createPageable with various ProductFilter parameters:")
    void testProductFilter(String description,
                           ProductFilter productFilter,
                           String expectedPredicate,
                           String expectedSort,
                           String expectedPageable) {
        //GIVEN + WHEN
        Predicate predicate = productFilter.createPredicate();
        Sort sort = productFilter.createSort();
        Pageable pageable = productFilter.createPageable();

        //THEN
        assertThat(predicate).isNotNull()
                             .hasToString(expectedPredicate);
        assertThat(sort).isNotNull()
                        .hasToString(expectedSort);
        assertThat(pageable).isNotNull()
                            .hasToString(expectedPageable)
                            .extracting(Pageable::getPageNumber, Pageable::getPageSize, Pageable::getSort)
                            .containsExactly(productFilter.pageNumber(), productFilter.pageSize(), sort);
    }
}
