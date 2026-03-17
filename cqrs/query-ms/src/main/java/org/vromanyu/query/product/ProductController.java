package org.vromanyu.query.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vromanyu.core.GetProductListResponse;
import org.vromanyu.core.GetProductResponse;

@RestController
@RequestMapping("/api/products/get")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetProductResponse> getProduct(@PathVariable Integer id) {
        logger.info("getProduct called with id: {}", id);
        GetProductResponse product = productService.getProductById(id);
        logger.info("product found: {}", product);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetProductListResponse> getAllProducts() {
        logger.info("getAllProducts called");
        GetProductListResponse products = productService.getAllProducts();
        logger.info("products size: {}", products.products().size());
        return ResponseEntity.ok(products);
    }

}
