package com.vromanyu.product;

import io.smallrye.mutiny.Uni;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.UpdateProductRequest;
import org.vromanyu.core.UpdateProductResponse;

public interface ProductService {
    Uni<CreateProductResponse> createProduct(Uni<CreateProductRequest> request);

    Uni<UpdateProductResponse> updateProduct(Integer productId, Uni<UpdateProductRequest> request);

    Uni<Void> deleteProduct(Integer productId);
}
