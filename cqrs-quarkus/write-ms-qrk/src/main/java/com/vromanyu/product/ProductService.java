package com.vromanyu.product;

import io.smallrye.mutiny.Uni;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;

public interface ProductService {
    Uni<CreateProductResponse> createProduct(Uni<CreateProductRequest> request);
}
