package org.vromanyu.write.product;

import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.UpdateProductRequest;
import org.vromanyu.core.UpdateProductResponse;

public interface ProductService {
    CreateProductResponse createProduct(CreateProductRequest request);
    UpdateProductResponse updateProduct(Integer productId, UpdateProductRequest request);
    void deleteProduct(Integer productId);
}
