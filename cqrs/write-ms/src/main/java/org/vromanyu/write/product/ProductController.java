package org.vromanyu.write.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.UpdateProductRequest;
import org.vromanyu.core.UpdateProductResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/products/write")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        logger.info("createProduct called with request: {}", request);
        CreateProductResponse response = productService.createProduct(request);
        logger.info("product created: {}", response);
        URI location = ServletUriComponentsBuilder.fromPath("/api/products/get/{id}").buildAndExpand(response.productId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable Integer id, @RequestBody UpdateProductRequest request) {
        logger.info("updateProduct called with productId: {}, request: {}", id, request);
        UpdateProductResponse response = productService.updateProduct(id, request);
        logger.info("product updated: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        logger.info("deleteProduct called with productId: {}", id);
        productService.deleteProduct(id);
        logger.info("product deleted");
        return ResponseEntity.noContent().build();
    }

}