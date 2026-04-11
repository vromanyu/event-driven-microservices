package com.vromanyu.product;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.vromanyu.core.*;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    EntityManager entityManager;

    private Product toProduct(CreateProductRequest request) {
        var product = new Product();
        product.name = request.productName();
        product.price = request.price();
        product.quantity = request.quantity();
        return product;
    }

    private UpdateProductResponse toUpdateProductResponse(Product product) {
        return new UpdateProductResponse(product.id,
                product.name,
                product.price,
                product.quantity);
    }

    private Optional<Product> findProductById(Integer productId) {
        return Optional.ofNullable(entityManager.find(Product.class, productId));
    }

    @Override
    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        Log.infof("createProduct called with request: %s", request);
        var product = toProduct(request);
        entityManager.persist(product);
        Log.infof("product created: %s", product);
        var productCreatedEvent = new ProductCreatedEvent(UUID.randomUUID().toString(),
                product.id,
                product.name,
                product.price,
                product.quantity);
        Log.infof("publishing product created event: %s", productCreatedEvent);
        // TO DO - publish product-created event
        return new CreateProductResponse(product.id, product.name, product.price, product.quantity);
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(Integer productId, UpdateProductRequest request) {
        Log.infof("updateProduct called with productId: %s, request: %s", productId, request);
        var product = findProductById(productId).orElseThrow(() -> new ProductNotFoundException(String.format("product with id %s not found", productId)));
        product.name = request.productName();
        product.price = request.price();
        product.quantity = request.quantity();
        var updatedProduct = entityManager.merge(product);
        Log.infof("product updated: %s", updatedProduct);
        var productUpdatedEvent = new ProductUpdatedEvent(UUID.randomUUID().toString(),
                updatedProduct.id,
                updatedProduct.name,
                updatedProduct.price,
                updatedProduct.quantity);
        Log.infof("publishing product updated event: %s", productUpdatedEvent);
        // TO DO - publish product-updated event
        return toUpdateProductResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) {
        Log.infof("deleteProduct called with productId: %s", productId);
        var product = findProductById(productId).orElseThrow(() -> new ProductNotFoundException(String.format("product with id %s not found", productId)));
        entityManager.remove(product);
        Log.infof("product deleted");
        var productDeletedEvent = new ProductDeletedEvent(UUID.randomUUID().toString(), productId);
        Log.infof("publishing product deleted event: %s", productDeletedEvent);
        // TO DO - publish product-deleted event
    }
}
