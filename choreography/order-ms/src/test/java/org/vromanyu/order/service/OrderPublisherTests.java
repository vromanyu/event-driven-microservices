package org.vromanyu.order.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class OrderPublisherTests {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private OrderPublisher orderPublisher;


    @Test
    public void whenSend_thenSuccess() {
        SendResult<String, Object> sendResult = Mockito.mock(SendResult.class);
        Mockito.when(kafkaTemplate.send(Mockito.any(), Mockito.anyString(), Mockito.any())).thenReturn(CompletableFuture.completedFuture(sendResult));

        orderPublisher.publishOrderCreatedEvent(1, null);

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.any(), Mockito.anyString(), Mockito.any());
    }

    @Test
    public void whenSend_thenFail() {
        CompletableFuture<SendResult<String, Object>> failedFuture =
                CompletableFuture.failedFuture(new RuntimeException("fail"));

        Mockito.when(kafkaTemplate.send(Mockito.any(), Mockito.anyString(), Mockito.any()))
                .thenReturn(failedFuture);

        orderPublisher.publishOrderCreatedEvent(1, null);

        Assertions.assertThat(failedFuture).isCompletedExceptionally();
    }
}
