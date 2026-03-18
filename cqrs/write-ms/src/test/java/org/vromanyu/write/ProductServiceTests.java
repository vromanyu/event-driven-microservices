package org.vromanyu.write;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vromanyu.core.CreateProductRequest;
import org.vromanyu.core.CreateProductResponse;
import org.vromanyu.core.UpdateProductRequest;
import org.vromanyu.core.UpdateProductResponse;
import org.vromanyu.write.product.Product;
import org.vromanyu.write.product.ProductEventPublisherServiceImpl;
import org.vromanyu.write.product.ProductRepository;
import org.vromanyu.write.product.ProductServiceImpl;

import java.time.OffsetDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    private Product mockedProduct;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductEventPublisherServiceImpl productEventPublisherService;

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
    public void givenProductRequest_whenCreateProduct_thenReturnProduct() {
        CreateProductRequest productRequest = new CreateProductRequest("cookies", 100, 10);

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(mockedProduct);
        Mockito.doNothing().when(productEventPublisherService).publishProductCreatedEvent(Mockito.any());

        CreateProductResponse response = productService.createProduct(productRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.productId()).isEqualTo(1);
        Assertions.assertThat(response.productName()).isEqualTo("cookies");
        Assertions.assertThat(response.price()).isEqualTo(100);
        Assertions.assertThat(response.quantity()).isEqualTo(10);
    }

    @Test
    public void givenProductRequest_whenPublishFails_throwException() {
        CreateProductRequest productRequest = new CreateProductRequest("cookies", 100, 10);

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(mockedProduct);
        Mockito.doThrow(RuntimeException.class).when(productEventPublisherService).publishProductCreatedEvent(Mockito.any());

        Assertions.assertThatThrownBy(() -> productService.createProduct(productRequest));
    }

    @Test
    public void givenValidProductIdAndUpdateProductRequest_whenUpdateProduct_thenReturnProduct() {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest("cookies", 150, 10);

        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockedProduct));
        mockedProduct.setPrice(150);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(mockedProduct);
        Mockito.doNothing().when(productEventPublisherService).publishProductUpdatedEvent(Mockito.any());

        UpdateProductResponse response = productService.updateProduct(1, updateProductRequest);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.productId()).isEqualTo(1);
        Assertions.assertThat(response.productName()).isEqualTo("cookies");
        Assertions.assertThat(response.price()).isEqualTo(150);
        Assertions.assertThat(response.quantity()).isEqualTo(10);
    }

    @Test
    public void givenInvalidProductId_whenUpdateProduct_thenThrowException() {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest("cookies", 150, 10);
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> productService.updateProduct(1, updateProductRequest));
    }

    @Test
    public void givenProductId_whenDeleteProduct_thenReturnNothing() {
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockedProduct));
        Mockito.doNothing().when(productEventPublisherService).publishProductDeletedEvent(Mockito.any());
        Assertions.assertThatCode(() -> productService.deleteProduct(1)).doesNotThrowAnyException();
        Mockito.verify(productRepository, Mockito.times(1)).findById(Mockito.anyInt());
        Mockito.verify(productRepository, Mockito.times(1)).delete(Mockito.any(Product.class));
    }

    @Test
    public void givenInvalidProductId_whenDeleteProduct_thenThrowException() {
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> productService.deleteProduct(1));
    }

    @Test
    public void givenValidProductId_whenPublishDeleteEvent_thenThrowException() {
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockedProduct));
        Mockito.doThrow(RuntimeException.class).when(productEventPublisherService).publishProductDeletedEvent(Mockito.any());
        Assertions.assertThatThrownBy(() -> productService.deleteProduct(1));
    }

}