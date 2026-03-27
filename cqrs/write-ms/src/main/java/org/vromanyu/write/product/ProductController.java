package org.vromanyu.write.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping(value = "/api/v{version}/products/write")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "create product",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "product created",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = CreateProductResponse.class)
                                    )
                            })
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "new product data",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateProductRequest.class)
                    ))
    )
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        logger.info("createProduct called with request: {}", request);
        CreateProductResponse response = productService.createProduct(request);
        logger.info("product created: {}", response);
        URI location = ServletUriComponentsBuilder.fromPath("/api/products/get/{id}").buildAndExpand(response.productId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(
            summary = "update product by id",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "product updated",
                            content = {
                                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = UpdateProductResponse.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "400",
                            description = "product not found",
                            content = @Content())
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "product updated data",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateProductRequest.class)
                    ))
    )
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable Integer id, @RequestBody UpdateProductRequest request) {
        logger.info("updateProduct called with productId: {}, request: {}", id, request);
        UpdateProductResponse response = productService.updateProduct(id, request);
        logger.info("product updated: {}", response);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "delete product by id",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "product successfully deleted",
                            content = {
                                    @Content()
                            }),
                    @ApiResponse(responseCode = "400",
                            description = "product not found",
                            content = @Content())
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        logger.info("deleteProduct called with productId: {}", id);
        productService.deleteProduct(id);
        logger.info("product deleted");
        return ResponseEntity.noContent().build();
    }

}