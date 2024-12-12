package service;

import com.iprody.com.productservice.persistence.entity.Currency;
import com.iprody.com.productservice.persistence.entity.DiscountEntity;
import com.iprody.com.productservice.persistence.entity.ProductEntity;
import com.iprody.com.productservice.persistence.repository.DiscountRepository;
import com.iprody.com.productservice.persistence.repository.ProductRepository;
import com.iprody.com.productservice.service.ProductFilter;
import com.iprody.com.productservice.service.ProductService;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    DiscountRepository discountRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    public void testCreateProduct() {
        ProductEntity testProduct = getTestProduct();
        when(productRepository.save(any(ProductEntity.class))).thenReturn(testProduct);
        ProductEntity createdProduct = productService.createProduct(testProduct);

        assertThat(createdProduct).isNotNull();
        assertThat(createdProduct.getSummary()).isEqualTo("testProduct");
        assertThat(createdProduct.getPrice()).isEqualByComparingTo(999);
        assertThat(createdProduct.isActive()).isEqualTo(true);
        assertThat(createdProduct.getCurrency()).isEqualTo(Currency.USD);
        assertThat(createdProduct.getDiscounts()).hasSize(0);
        assertThat(createdProduct.getDuration()).isEqualTo(30);
        assertThat(createdProduct.getDescription()).isNotNull();
        assertThat(createdProduct.getCreatedAt()).isNotNull();
        assertThat(createdProduct.getUpdatedAt()).isEqualTo(createdProduct.getCreatedAt());
    }

    @Test
    public void testCreateDiscount() {
        DiscountEntity createdDiscount = getTestDiscount();
        when(discountRepository.save(any(DiscountEntity.class))).thenReturn(createdDiscount);

        productService.createDiscount(createdDiscount);

        assertThat(createdDiscount).isNotNull();
        assertThat(createdDiscount.getProduct()).isNotNull();
        assertThat(createdDiscount.getSummary()).isEqualTo("testDiscount");
        assertThat(createdDiscount.getActive()).isFalse();
        assertThat(createdDiscount.getCreatedAt()).isBefore(Instant.now());
        assertThat(createdDiscount.getUpdatedAt()).isEqualTo(createdDiscount.getCreatedAt());
        assertThat(createdDiscount.getValue()).isEqualTo(10);
        assertThat(createdDiscount.getFromTime()).isAfter(createdDiscount.getCreatedAt());
//        assertThat(createdDiscount.getUntilTime().isAfter(createdDiscount.getFromTime());
    }

    @Test
    public void testGetProductById() {
        ProductEntity testProduct = getTestProduct();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(testProduct));

        ProductEntity productToFind = productService.getProductById(testProduct.getId());

        assertThat(productToFind).isNotNull();
    }

    @Test
    public void findAllProducts() {
        List<ProductEntity> products = getListProducts();
        Page<ProductEntity> productPage = new PageImpl<>(products, PageRequest.of(0, 25), products.size());

        when(productRepository.findAll(any(Predicate.class), any(Pageable.class))).thenReturn(productPage);

        ProductFilter filter = new ProductFilter("testProduct",true, ASC, ASC,0,25 );

        Page<ProductEntity> productResult = productService.findAllProducts(filter);

        assertThat(productResult).isNotNull();
        assertThat(productResult).hasSize(2);
        assertThat(productResult).containsExactly(products.get(0), products.get(1));

    }

    @Test
    public void testUpdateProduct() {
        ProductEntity testProduct = getTestProduct();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(testProduct));
//        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        ProductEntity productForUpdate = new ProductEntity();
        productForUpdate.setId(testProduct.getId());
        productForUpdate.setSummary("OneMoreTestProduct");
        productForUpdate.setDescription("Additional product for testing");
        productForUpdate.setPrice(777);
        productForUpdate.setCurrency(Currency.EUR);
        productForUpdate.setDuration(90);
        productForUpdate.setActive(true);
        productForUpdate.setUpdatedAt(Instant.now());

        ProductEntity updatedProduct = productService.updateProduct(productForUpdate);
//        assertThat(updatedProduct.getUpdatedAt()).isAfter(updatedProduct.getCreatedAt());
        assertThat(updatedProduct.getCurrency()).isEqualTo(Currency.EUR);
//        assertThat(updatedProduct.getSummary()).isEqualTo("OneMoreTestProduct");

    }

    @Test
    public void testApplyDiscountToProduct() {
        ProductEntity testProduct = getTestProduct();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(testProduct));

        DiscountEntity discountForApply = getTestDiscount();

        ProductEntity updatedProduct = productService.applyDiscountToProduct(testProduct.getId(), discountForApply);

        assertThat(updatedProduct.getDiscounts()).hasSize(1);
    }

    @Test
    public void testApplyDiscountToProducts() {
        List<ProductEntity> products = getListProducts();
        when(productRepository.findAllById(anyList())).thenReturn(products);

        List<UUID> productIds = new ArrayList<>(2);
        productIds.add(UUID.randomUUID());
        productIds.add(UUID.randomUUID());

        List<ProductEntity> updatedProducts = productService.applyDiscountToProducts(productIds, getTestDiscount());

        assertThat(updatedProducts).isNotNull()
                                   .hasSize(2);
        assertThat(updatedProducts.getFirst().getDiscounts()).hasSize(1);
    }

    @Test
    public void testChangeProductStatus() {
        ProductEntity testProduct = getTestProduct();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(testProduct));

        ProductEntity updatedProduct = productService.changeProductStatus(testProduct.getId(), false);

        assertThat(updatedProduct.isActive()).isEqualTo(false);
    }

    @Test
    public void testChangeProductPriceAndCurrency() {
        ProductEntity productForUpdate = getTestProduct();
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.of(productForUpdate));

        ProductEntity updatedProduct = productService.changeProductPriceAndCurrency(productForUpdate.getId(),777,Currency.EUR);

        assertThat(updatedProduct.getPrice()).isEqualTo(777);
        assertThat(updatedProduct.getCurrency()).isEqualTo(Currency.EUR);

    }

    @Test
    public void testChangeDiscountStatus(){
        DiscountEntity discount = getTestDiscount();
        when(discountRepository.findById(any(UUID.class))).thenReturn(Optional.of(discount));

        DiscountEntity updatedDiscount = productService.changeDiscountStatus(discount.getId(), true);

        assertThat(updatedDiscount.getActive()).isEqualTo(true);
    }

    private ProductEntity getTestProduct() {
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID());
        product.setSummary("testProduct");
        product.setDescription("A new product for testing");
        product.setPrice(999);
        product.setCurrency(Currency.USD);
        product.setDuration(30);
        product.setActive(true);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(product.getCreatedAt());

        return product;
    }


    private DiscountEntity getTestDiscount() {
        DiscountEntity discount = new DiscountEntity();
        discount.setId(UUID.randomUUID());
        discount.setSummary("testDiscount");
        discount.setActive(false);
        discount.setCreatedAt(Instant.now());
        discount.setUpdatedAt(discount.getCreatedAt());
        discount.setValue(10);
        discount.setProduct(getTestProduct());
        discount.setFromTime(Instant.now());
        discount.setUntilTime(Instant.now().plus(30, ChronoUnit.DAYS));

        return discount;
    }

    private List<ProductEntity> getListProducts() {
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID());
        product.setSummary("testProduct");
        product.setDescription("A new product for testing");
        product.setPrice(999);
        product.setCurrency(Currency.USD);
        product.setDuration(30);
        product.setActive(true);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        ProductEntity oneMoreProduct = new ProductEntity();
        oneMoreProduct.setId(UUID.randomUUID());
        oneMoreProduct.setSummary("OneMoreTestProduct");
        oneMoreProduct.setDescription("Additional product for testing");
        oneMoreProduct.setPrice(777);
        oneMoreProduct.setCurrency(Currency.EUR);
        oneMoreProduct.setDuration(90);
        oneMoreProduct.setActive(true);
        oneMoreProduct.setCreatedAt(Instant.now());
        oneMoreProduct.setUpdatedAt(Instant.now());

        List<ProductEntity> products = new ArrayList<>();
        products.add(product);
        products.add(oneMoreProduct);

        return products;
    }
}



