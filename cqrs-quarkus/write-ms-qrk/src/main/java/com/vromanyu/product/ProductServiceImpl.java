package com.vromanyu.product;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.transactions.KafkaTransactions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.hibernate.reactive.mutiny.Mutiny;
import org.vromanyu.core.*;

import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    Mutiny.SessionFactory sf;

    @Channel("product-created-channel")
    KafkaTransactions<ProductCreatedEvent> productCreatedEventChannel;

    @Channel("product-updated-channel")
    KafkaTransactions<ProductUpdatedEvent> productUpdatedEventChannel;

    @Channel("product-deleted-channel")
    KafkaTransactions<ProductDeletedEvent> productDeletedEventChannel;

    private Product toProduct(CreateProductRequest request) {
        var product = new Product();
        product.name = request.productName();
        product.price = request.price();
        product.quantity = request.quantity();
        return product;
    }

    private Uni<Product> findProductById(Integer productId) {
        return sf.withTransaction(session -> session.find(Product.class, productId));
    }


    @Override
    public Uni<CreateProductResponse> createProduct(Uni<CreateProductRequest> request) {
        return request.map(this::toProduct).flatMap(productEntity -> sf.withTransaction(session -> session.persist(productEntity).replaceWith(productEntity).call(savedProduct -> productCreatedEventChannel.withTransaction(emitter -> {
            var event = new ProductCreatedEvent(UUID.randomUUID().toString(), savedProduct.id, savedProduct.name, savedProduct.price, savedProduct.quantity);
            emitter.send(event);
            return Uni.createFrom().voidItem();
        }))).onItem().transform(savedProduct -> new CreateProductResponse(savedProduct.id, savedProduct.name, savedProduct.price, savedProduct.quantity)));
    }

    @Override
    public Uni<UpdateProductResponse> updateProduct(Integer productId, Uni<UpdateProductRequest> request) {
        return request.flatMap(updateProductRequest -> sf.withTransaction(session -> {
            Uni<Product> productEntity = findProductById(productId).onItem().ifNull().failWith(() -> new ProductNotFoundException(String.format("product with id %s not found", productId)));
            return productEntity.call(product -> {
                product.name = updateProductRequest.productName();
                product.price = updateProductRequest.price();
                product.quantity = updateProductRequest.quantity();
                return session.merge(product).call(updatedProduct -> productUpdatedEventChannel.withTransaction(emitter -> {
                    var event = new ProductUpdatedEvent(UUID.randomUUID().toString(), updatedProduct.id, updatedProduct.name, updatedProduct.price, updatedProduct.quantity);
                    emitter.send(event);
                    return Uni.createFrom().voidItem();
                }));
            });
        })).map(updatedProduct -> new UpdateProductResponse(updatedProduct.id, updatedProduct.name, updatedProduct.price, updatedProduct.quantity));
    }

    @Override
    public Uni<Void> deleteProduct(Integer productId) {
        return sf.withTransaction(session -> {
            Uni<Product> nullableProduct = findProductById(productId);
            Uni<Product> productEntity = nullableProduct.onItem().ifNull().failWith(() -> new ProductNotFoundException(String.format("product with id %s not found", productId)));
            return productEntity.call(product -> productDeletedEventChannel.withTransaction(emitter -> {
                var event = new ProductDeletedEvent(UUID.randomUUID().toString(), product.id);
                emitter.send(event);
                return Uni.createFrom().voidItem();
            }).flatMap(v -> session.remove(product)));
        }).replaceWithVoid();
    }
}
