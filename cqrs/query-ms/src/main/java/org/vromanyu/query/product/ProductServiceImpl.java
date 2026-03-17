package org.vromanyu.query.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vromanyu.core.GetProductListResponse;
import org.vromanyu.core.GetProductResponse;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public GetProductResponse getProductById(Integer productId) {
        logger.info("getProductById called with productId: {}", productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product with id " + productId + " not found"));
        logger.info("product found: {}", product);
        return toGetProductResponse(product);
    }

    @Override
    public GetProductListResponse getAllProducts() {
        logger.info("getAllProducts called");
        List<GetProductResponse> productResponses = productRepository.findAll().stream().map(this::toGetProductResponse).toList();
        logger.info("products size: {}", productResponses.size());
        return new GetProductListResponse(productResponses);
    }

    private GetProductResponse toGetProductResponse(Product product) {
        return new GetProductResponse(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
    }
}
