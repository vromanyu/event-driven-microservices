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
import org.springframework.cache.CacheManager;
import org.vromanyu.core.ProductCreatedEvent;
import org.vromanyu.core.ProductDeletedEvent;
import org.vromanyu.core.ProductUpdatedEvent;
import org.vromanyu.query.product.Product;
import org.vromanyu.query.product.ProductEventConsumerServiceImpl;
import org.vromanyu.query.product.ProductRepository;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ProductEventConsumerServiceTests {

    private Product mockedProduct;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductEventConsumerServiceImpl productEventConsumerService;

    @BeforeEach
    public void setUp() {
        mockedProduct = new Product();
        mockedProduct.setId(1);
        mockedProduct.setName("cookies");
        mockedProduct.setPrice(100);
        mockedProduct.setQuantity(10);
    }

    @AfterEach
    public void tearDown() {
        mockedProduct = null;
    }

    @Test
    public void whenProductCreatedEvent_thenProductSaved() {
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                UUID.randomUUID().toString(),
                1,
                "cookies",
                100,
                10
        );

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(mockedProduct);

        Assertions.assertThatCode(() -> productEventConsumerService.consumeProductCreatedEvent(productCreatedEvent)).doesNotThrowAnyException();
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void whenProductUpdatedEvent_thenUpdateProduct() {
        ProductUpdatedEvent productUpdateEvent = new ProductUpdatedEvent(
                UUID.randomUUID().toString(),
                1,
                "cookies",
                100,
                10
        );

        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockedProduct));

        Assertions.assertThatCode(() -> productEventConsumerService.consumeProductUpdatedEvent(productUpdateEvent)).doesNotThrowAnyException();
        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void whenProductUpdatedEvent_thenThrowException() {
        ProductUpdatedEvent productUpdateEvent = new ProductUpdatedEvent(
                UUID.randomUUID().toString(),
                1,
                "cookies",
                100,
                10
        );

        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> productEventConsumerService.consumeProductUpdatedEvent(productUpdateEvent));
    }

    @Test
    public void whenProductDeletedEvent_thenSuccess() {
        ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(UUID.randomUUID().toString(), 1);
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(mockedProduct));
        Assertions.assertThatCode(() -> productEventConsumerService.consumeProductDeletedEvent(productDeletedEvent)).doesNotThrowAnyException();
    }

    @Test
    public void whenProductDeletedEvent_thenThrowException() {
        ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(UUID.randomUUID().toString(), 1);
        Mockito.when(productRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> productEventConsumerService.consumeProductDeletedEvent(productDeletedEvent));
    }

}
