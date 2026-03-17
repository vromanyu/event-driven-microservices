package org.vromanyu.write.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.UpdateProductRequest;
import org.vromanyu.core.UpdateProductResponse;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        return toCreateProductResponse(savedProduct);
    }

    @Override
    public UpdateProductResponse updateProduct(Integer productId, UpdateProductRequest request) {
        logger.info("updateProduct called with productId: {}, request: {}", productId, request);
        Product product = findProductById(productId);
        product.setName(request.productName());
        product.setPrice(request.price());
        product.setQuantity(request.quantity());
        Product updatedProduct = productRepository.save(product);
        logger.info("product updated: {}", updatedProduct);
        return toUpdateProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Integer productId) {
        logger.info("deleteProduct called with productId: {}", productId);
        Product product = findProductById(productId);
        productRepository.delete(product);
        logger.info("product deleted");
    }
}
