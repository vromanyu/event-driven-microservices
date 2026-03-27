package org.vromanyu.query.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/api/v{version}/products/get")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "get product by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "product found",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = GetProductResponse.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "400",
                            description = "product not found",
                            content = {
                                    @Content()
                            }),
                    @ApiResponse(responseCode = "403",
                            description = "forbidden",
                            content = {
                                    @Content()
                            })
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Parameter(required = true, name = "version", description = "API Version", in = ParameterIn.PATH, schema = @Schema(implementation = Integer.class, defaultValue = "1"))
    public ResponseEntity<GetProductResponse> getProduct(@PathVariable Integer id) {
        logger.info("getProduct called with id: {}", id);
        GetProductResponse product = productService.getProductById(id);
        logger.info("product found: {}", product);
        return ResponseEntity.ok(product);
    }

    @Operation(
            summary = "get all available products",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "all products",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = GetProductListResponse.class))
                            }),
                    @ApiResponse(responseCode = "403",
                            description = "forbidden",
                            content = {
                                    @Content()
                            })
            }
    )
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Parameter(required = true, name = "version", description = "API Version", in = ParameterIn.PATH, schema = @Schema(implementation = Integer.class, defaultValue = "1"))
    public ResponseEntity<GetProductListResponse> getAllProducts() {
        logger.info("getAllProducts called");
        GetProductListResponse products = productService.getAllProducts();
        logger.info("products size: {}", products.products().size());
        return ResponseEntity.ok(products);
    }

}
