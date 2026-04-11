package com.vromanyu.product;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.transactions.KafkaTransactions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.hibernate.reactive.mutiny.Mutiny;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.ProductCreatedEvent;

import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    Mutiny.SessionFactory sf;

    @Channel("product-created-channel")
    KafkaTransactions<ProductCreatedEvent> productCreatedEventChannel;

    private Product toProduct(CreateProductRequest request) {
        var product = new Product();
        product.name = request.productName();
        product.price = request.price();
        product.quantity = request.quantity();
        return product;
    }


    @Override
    public Uni<CreateProductResponse> createProduct(Uni<CreateProductRequest> request) {
        return request.map(this::toProduct)
                .flatMap(productEntity -> sf.withTransaction(session ->
                                session.persist(productEntity)
                                        .replaceWith(productEntity)
                                        .call(savedProduct -> productCreatedEventChannel.withTransaction(emitter -> {
                                                    var event = new ProductCreatedEvent(
                                                            UUID.randomUUID().toString(),
                                                            savedProduct.id,
                                                            savedProduct.name,
                                                            savedProduct.price,
                                                            savedProduct.quantity
                                                    );
                                                    emitter.send(event);
                                                    return Uni.createFrom().voidItem();
                                                })
                                        ))
                        .onItem().transform(savedProduct -> new CreateProductResponse(savedProduct.id, savedProduct.name, savedProduct.price, savedProduct.quantity)));
    }

}
