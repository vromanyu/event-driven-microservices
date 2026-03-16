package org.vromanyu.payment.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.vromanyu.core.OrderEvent;
import org.vromanyu.core.OrderRequestDto;
import org.vromanyu.core.OrderStatus;
import org.vromanyu.payment.entity.UserBalance;
import org.vromanyu.payment.entity.UserTransaction;
import org.vromanyu.payment.repository.UserBalanceRepository;
import org.vromanyu.payment.repository.UserTransactionRepository;

import java.time.LocalDate;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTests {

    @Mock
    private PaymentPublisher paymentPublisher;

    @Mock
    private UserBalanceRepository userBalanceRepository;

    @Mock
    private UserTransactionRepository userTransactionRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    public void whenValidPaymentEvent_thenSuccess() {
        OrderEvent orderEvent = new OrderEvent(
                "1",
                1,
                LocalDate.now(),
                new OrderRequestDto(1, 1, 1),
                OrderStatus.CREATED
        );

        UserBalance userBalance = new UserBalance();
        userBalance.setId(1);
        userBalance.setUserId(1);
        userBalance.setPrice(1000);

        Mockito.when(userBalanceRepository.findByUserId(Mockito.anyInt())).thenReturn(
                Collections.singletonList(userBalance));
        Mockito.when(userTransactionRepository.save(Mockito.any())).thenReturn(new UserTransaction());

        Assertions.assertThatCode(() -> paymentService.pay(orderEvent)).doesNotThrowAnyException();
    }

    @Test
    public void whenInvalidOrderStatus_thenThrowException() {
        OrderEvent orderEvent = new OrderEvent(
                "1",
                1,
                LocalDate.now(),
                new OrderRequestDto(1, 1, 1),
                OrderStatus.CANCELLED
        );

        Assertions.assertThatThrownBy(() -> paymentService.pay(orderEvent));
    }

    @Test
    public void whenInvalidUserBalance_thenThrowException() {
        OrderEvent orderEvent = new OrderEvent(
                "1",
                1,
                LocalDate.now(),
                new OrderRequestDto(1, 1, 100000),
                OrderStatus.CREATED
        );

        UserBalance userBalance = new UserBalance();
        userBalance.setId(1);
        userBalance.setUserId(1);
        userBalance.setPrice(1);

        Mockito.when(userBalanceRepository.findByUserId(Mockito.anyInt())).thenReturn(
                Collections.singletonList(userBalance));

        Assertions.assertThatThrownBy(() -> paymentService.pay(orderEvent));
    }

}
