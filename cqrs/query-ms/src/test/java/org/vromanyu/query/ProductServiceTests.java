package org.vromanyu.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vromanyu.core.GetProductListResponse;
import org.vromanyu.core.GetProductResponse;
import org.vromanyu.query.product.Product;
import org.vromanyu.query.product.ProductRepository;
import org.vromanyu.query.product.ProductServiceImpl;

import java.time.OffsetDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    private Product mockedProduct;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        mockedProduct = new Product();
        mockedProduct.setId(1);
        mockedProduct.setName("cookies");
        mockedProduct.setPrice(100);
        mockedProduct.setQuantity(10);
        mockedProduct.setCreatedAt(OffsetDateTime.now());
        mockedProduct.setUpdatedAt(OffsetDateTime.now());
    }

    @AfterEach
    public void tearDown() {
        mockedProduct = null;
    }

    @Test
    public void givenValidProductId_whenFindById_thenReturnProduct() {
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockedProduct));

        GetProductResponse productResponse = productService.getProductById(1);

        Assertions.assertThat(productResponse).isNotNull();
        Assertions.assertThat(productResponse.productId()).isEqualTo(1);
        Assertions.assertThat(productResponse.productName()).isEqualTo("cookies");
        Assertions.assertThat(productResponse.price()).isEqualTo(100);
        Assertions.assertThat(productResponse.quantity()).isEqualTo(10);
    }

    @Test
    public void givenInvalidProductId_whenFindById_thenThrowException() {
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> productService.getProductById(1));
    }

    @Test
    public void givenNoProducts_whenFindAll_thenReturnEmptyList() {
        Mockito.when(productRepository.findAll()).thenReturn(java.util.List.of());

        GetProductListResponse allProducts = productService.getAllProducts();

        Assertions.assertThat(allProducts).isNotNull();
        Assertions.assertThat(allProducts.products()).isNotNull();
        Assertions.assertThat(allProducts.products()).isEmpty();
    }

    @Test
    public void givenProducts_whenFindAll_thenReturnListOfProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(java.util.List.of(mockedProduct));

        GetProductListResponse allProducts = productService.getAllProducts();

        Assertions.assertThat(allProducts).isNotNull();
        Assertions.assertThat(allProducts.products()).isNotNull();
        Assertions.assertThat(allProducts.products().size()).isGreaterThan(0);
    }

}
