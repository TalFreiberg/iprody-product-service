package com.iprody.com.productservice.service;

import com.iprody.com.productservice.persistence.entity.Currency;
import com.iprody.com.productservice.persistence.entity.DiscountEntity;
import com.iprody.com.productservice.persistence.entity.ProductEntity;
import com.iprody.com.productservice.persistence.repository.DiscountRepository;
import com.iprody.com.productservice.persistence.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public DiscountEntity createDiscount(DiscountEntity discountEntity) {
        return discountRepository.save(discountEntity);
    }

    private DiscountEntity getDiscountByIdOrElseThrow(UUID id) {
        return discountRepository.findById(id)
                                 .orElseThrow(() -> new EntityNotFoundException(DiscountEntity.class.getSimpleName()));
    }
    private ProductEntity getByIdOrElseThrow(UUID id) {
        return productRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(ProductEntity.class.getSimpleName()));
    }

    public ProductEntity getProductById (UUID id) {
        return getByIdOrElseThrow(id);
    }

    public Page<ProductEntity> findAllProducts(ProductFilter filter) {
        return productRepository.findAll(filter.createPredicate(), filter.createPageable());
    }

    @Transactional
    public ProductEntity updateProduct(ProductEntity product) {
        ProductEntity existingProduct = getByIdOrElseThrow(product.getId());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCurrency(product.getCurrency());
        existingProduct.setDiscounts(product.getDiscounts());
        existingProduct.setActive(product.isActive());
        existingProduct.setUpdatedAt(Instant.now());
        return existingProduct;
    }

    @Transactional
    public ProductEntity applyDiscountToProduct(UUID productId, DiscountEntity discount) {
        ProductEntity product = getByIdOrElseThrow(productId);
        product.getDiscounts().add(discount);
        return product;
    }

     @Transactional
        public List<ProductEntity> applyDiscountToProducts(List<UUID> productIds, DiscountEntity discount) {
        return productRepository.findAllById(productIds)
                         .stream()
                         .peek(p -> p.getDiscounts().add(discount))
                         .toList();
        }

    @Transactional
    public DiscountEntity changeDiscountStatus(UUID discountID, Boolean discountStatus) {
        DiscountEntity discount = discountRepository.findById(discountID).orElseThrow(EntityNotFoundException::new);
        discount.setActive(discountStatus);
        return discount;
    }

    @Transactional
    public ProductEntity changeProductStatus(UUID id, Boolean active) {
        ProductEntity product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setActive(active);
        return product;
    }

    @Transactional
    public ProductEntity changeProductPriceAndCurrency(UUID id, int price, Currency currency) {
        ProductEntity product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        product.setPrice(price);
        product.setCurrency(currency);
        product.setUpdatedAt(Instant.now());
        return product;
    }

}