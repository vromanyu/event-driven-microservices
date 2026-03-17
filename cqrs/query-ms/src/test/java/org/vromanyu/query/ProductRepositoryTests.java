package org.vromanyu.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.vromanyu.query.product.Product;
import org.vromanyu.query.product.ProductRepository;

import java.util.List;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private List<Product> products;

    @BeforeEach
    public void setUp() {
        product = new Product("cookies", 100, 10);
        products = List.of(
                new Product("cookies", 100, 10),
                new Product("donuts", 200, 20),
                new Product("ice cream", 300, 30)
        );
    }

    @AfterEach
    public void tearDown() {
        product = null;
        products = null;
    }

    @Test
    public void givenValidProductId_whenFindById_thenReturnProduct() {
        Product savedProduct = productRepository.save(product);

        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);

        Assertions.assertThat(foundProduct).isNotNull();
        Assertions.assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
    }

    @Test
    public void givenInvalidProductId_whenFindById_thenThrowException() {
        Assertions.assertThatThrownBy(() -> productRepository.findById(0).orElseThrow());
    }

    @Test
    public void givenProducts_whenFindAll_thenReturnProducts() {
        productRepository.saveAll(products);

        List<Product> foundProducts = productRepository.findAll();

        Assertions.assertThat(foundProducts).isNotNull();
        Assertions.assertThat(foundProducts.size()).isGreaterThan(0);
    }

    @Test
    public void givenNoProducts_whenFindAll_thenReturnEmptyList() {
        List<Product> foundProducts = productRepository.findAll();

        Assertions.assertThat(foundProducts).isNotNull();
        Assertions.assertThat(foundProducts).isEmpty();
    }

}
