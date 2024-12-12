package com.iprody.com.productservice.controller;

import com.iprody.com.productservice.dto.DiscountDto;
import com.iprody.com.productservice.dto.ProductCreateDto;
import com.iprody.com.productservice.dto.ProductPriceCurrencyUpdateDto;
import com.iprody.com.productservice.dto.ProductUpdateDto;
import com.iprody.com.productservice.persistence.entity.DiscountEntity;
import com.iprody.com.productservice.persistence.entity.ProductEntity;
import com.iprody.com.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping(value = ProductController.REST_URL)
public class ProductController {

    final static String REST_URL = "/product";
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Add new Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product was successfully created",
                    content = @Content(schema = @Schema(implementation = ProductCreateDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PutMapping("/create")
    public void createProduct(@Valid @RequestBody ProductCreateDto productDto) {
        ProductEntity product = new ProductEntity();
        product.setSummary(productDto.getSummary());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCurrency(productDto.getCurrency());
        product.setDuration(productDto.getDuration());
        product.setActive(productDto.isActive());
        service.createProduct(product);
    }

    @Operation(summary = "Update existed Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Product was successfully updated",
                    content = @Content(schema = @Schema(implementation = ProductUpdateDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PostMapping("/update/{productId}")
    public void updateProduct(@PathVariable UUID productId,
                              @Valid @RequestBody
                              ProductUpdateDto updateDto) {
        ProductEntity updatedProduct = new ProductEntity();
        updatedProduct.setId(productId);
        if (null != updateDto.getSummary()) {
            updatedProduct.setSummary(updateDto.getSummary());
        }
        if (null != updateDto.getDescription()) {
            updatedProduct.setDescription(updateDto.getDescription());
        }
        if (null != updateDto.getCurrency()) {
            updatedProduct.setCurrency(updateDto.getCurrency());
        }
        if (null != updateDto.getPrice()) {
            updatedProduct.setPrice(updateDto.getPrice());
        }
        if (null != updateDto.getDuration()) {
            updatedProduct.setDuration(updateDto.getDuration());
        }
        service.updateProduct(updatedProduct);
    }

    @Operation(summary = "Get Product by guid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Product found and returned",
                    content = @Content(schema = @Schema(implementation = ProductEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @GetMapping("/get/{productId}")
    public ProductEntity findProductById(@PathVariable UUID productId) {
        return service.getProductById(productId);
    }

    @Operation(summary = "Add new Discount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount was successfully created",
                    content = @Content(schema = @Schema(implementation = DiscountDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred")
    })
    @PostMapping("/discount/save")
    public void createDiscount(@RequestBody DiscountDto discountDto) {
        DiscountEntity entity = new DiscountEntity();

        entity.setValue(discountDto.getValue());
        entity.setSummary(discountDto.getSummary());
        entity.setActive(discountDto.getActive());
        entity.setFromTime(discountDto.getFromTime());
        entity.setUntilTime(discountDto.getUntilTime());
        var relatedProduct = new ProductEntity();
        relatedProduct.setId(discountDto.getProductId());
        entity.setProduct(relatedProduct);

        service.createDiscount(entity);
    }

    @PostMapping("/discount/apply/{productId}")
    public void applyDiscount(@PathVariable UUID productId,
                              @Valid @RequestBody DiscountDto discount) {
        DiscountEntity discountToUpdate = new DiscountEntity();
        discountToUpdate.setValue(discount.getValue());
        discountToUpdate.setActive(discount.getActive());
        discountToUpdate.setSummary(discount.getSummary());
        discountToUpdate.setUntilTime(discount.getUntilTime());
        discountToUpdate.setFromTime(discount.getFromTime());
        discountToUpdate.setProduct(discount.getProduct());

        service.applyDiscountToProduct(productId, discountToUpdate);
    }

    @PostMapping("/update/status/{productId}/{newStatus}")
    public void changeProductStatus(@PathVariable UUID productId,
                                    @PathVariable @NotNull Boolean newStatus) {
        service.changeProductStatus(productId, newStatus);
    }

    @PostMapping("/update/discount/{discountId}/{newStatus}")
    public void changeDiscountStatus(@PathVariable UUID discountId,
                                     @PathVariable @NotNull Boolean newStatus) {

        service.changeDiscountStatus(discountId, newStatus);
    }

    @PostMapping("/update/price/currency/{productId}")
    public void changePriceAndCurrency(@PathVariable UUID productId,
                                       @Valid @RequestBody
                                       ProductPriceCurrencyUpdateDto updateDto){
        ProductEntity updatedProduct = new ProductEntity();
        if (null != updateDto.getPrice()) {
            updatedProduct.setPrice(updateDto.getPrice());
        }
        if (null != updateDto.getCurrency()) {
            updatedProduct.setCurrency(updateDto.getCurrency());
        }
        service.changeProductPriceAndCurrency(productId, updatedProduct.getPrice(), updatedProduct.getCurrency());
    }

//    @GetMapping("/find/all/{filter}/{sortBy}/{sortDirection}")
//    public List<ProductEntity> findAllProducts() {
//        ProductFilter filter = new ProductFilter();
//        return service.findAllProducts(filter);

}