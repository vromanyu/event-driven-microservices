package org.vromanyu.write.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.*;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductEventPublisherService productEventPublisherService;

    public ProductServiceImpl(ProductRepository productRepository, ProductEventPublisherService productEventPublisherService) {
        this.productRepository = productRepository;
        this.productEventPublisherService = productEventPublisherService;
    }

    private Product findProductById(Integer productId) {
        return productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product with id " + productId + " not found"));
    }

    private Product toProduct(CreateProductRequest request) {
        return new Product(request.productName(), request.price(), request.quantity());
    }

    private CreateProductResponse toCreateProductResponse(Product product) {
        return new CreateProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    private UpdateProductResponse toUpdateProductResponse(Product product) {
        return new UpdateProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }

    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        logger.info("createProduct called with request: {}", request);
        Product newProduct = toProduct(request);
        Product savedProduct = productRepository.save(newProduct);
        logger.info("product saved: {}", savedProduct);
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(UUID.randomUUID().toString(),
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getQuantity());
        logger.info("publishing product created event: {}", productCreatedEvent);
        productEventPublisherService.publishProductCreatedEvent(productCreatedEvent);
        return toCreateProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(Integer productId, UpdateProductRequest request) {
        logger.info("updateProduct called with productId: {}, request: {}", productId, request);
        Product product = findProductById(productId);
        product.setName(request.productName());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        Product updatedProduct = productRepository.save(product);
        logger.info("product updated: {}", updatedProduct);
        ProductUpdatedEvent productUpdatedEvent = new ProductUpdatedEvent(UUID.randomUUID().toString(),
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                updatedProduct.getQuantity());
        logger.info("publishing product updated event: {}", productUpdatedEvent);
        productEventPublisherService.publishProductUpdatedEvent(productUpdatedEvent);
        return toUpdateProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        logger.info("deleteProduct called with productId: {}", productId);
        Product product = findProductById(productId);
        productRepository.delete(product);
        logger.info("product deleted");
        ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(UUID.randomUUID().toString(), productId);
        logger.info("publishing product deleted event: {}", productDeletedEvent);
        productEventPublisherService.publishProductDeletedEvent(productDeletedEvent);
    }
}
