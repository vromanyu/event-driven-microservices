package org.vromanyu.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.OrderEvent;
import org.vromanyu.core.OrderStatus;
import org.vromanyu.core.PaymentRequestDto;
import org.vromanyu.payment.entity.UserBalance;
import org.vromanyu.payment.entity.UserTransaction;
import org.vromanyu.payment.repository.UserBalanceRepository;
import org.vromanyu.payment.repository.UserTransactionRepository;

@Service
@KafkaListener(topics = "${kafka.topics.order}")
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final UserBalanceRepository userBalanceRepository;
    private final UserTransactionRepository userTransactionRepository;
    private final PaymentPublisher paymentPublisher;

    public PaymentServiceImpl(UserBalanceRepository userBalanceRepository, UserTransactionRepository userTransactionRepository, PaymentPublisher paymentPublisher) {
        this.userBalanceRepository = userBalanceRepository;
        this.userTransactionRepository = userTransactionRepository;
        this.paymentPublisher = paymentPublisher;
    }

    @Override
    @KafkaHandler
    @Transactional
    public void pay(@Payload OrderEvent orderEvent) {
        logger.info("received order event: {}", orderEvent);

        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(
                orderEvent.orderId(),
                orderEvent.orderRequestDto().userId(),
                orderEvent.orderRequestDto().amount()
        );

        if (!orderEvent.orderStatus().equals(OrderStatus.CREATED)) {
            paymentPublisher.sendFailedEvent(paymentRequestDto, orderEvent);
            throw new RuntimeException("order status is not CREATED");
        }

        UserBalance userBalance = userBalanceRepository.findByUserId(orderEvent.orderRequestDto().userId())
                .stream()
                .findFirst().orElseThrow(() -> {
                    paymentPublisher.sendFailedEvent(paymentRequestDto, orderEvent);
                    return new RuntimeException("user with id " + orderEvent.orderRequestDto().userId() + " not found");
                });
        if (userBalance.getPrice() < orderEvent.orderRequestDto().amount()) {
            paymentPublisher.sendFailedEvent(paymentRequestDto, orderEvent);
            throw new RuntimeException("user balance " + userBalance.getPrice() + " is not enough");
        }

        userBalance.setPrice(userBalance.getPrice() - orderEvent.orderRequestDto().amount());
        userBalanceRepository.save(userBalance);

        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(orderEvent.orderRequestDto().userId());
        userTransaction.setOrderId(orderEvent.orderId());
        userTransaction.setAmount(orderEvent.orderRequestDto().amount());
        userTransactionRepository.save(userTransaction);

        paymentPublisher.sendPaidEvent(paymentRequestDto, orderEvent);
    }
}
