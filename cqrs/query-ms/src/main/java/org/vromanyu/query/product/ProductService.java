package org.vromanyu.query.product;

import org.vromanyu.core.GetProductListResponse;
import org.vromanyu.core.GetProductResponse;

public interface ProductService {
    GetProductResponse getProductById(Integer productId);
    GetProductListResponse getAllProducts();
}
