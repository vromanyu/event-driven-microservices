package org.vromanyu.payment.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.vromanyu.payment.entity.UserTransaction;

import java.util.List;

@DataJpaTest
public class UserTransactionRepositoryTests {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @Test
    public void givenUserTransaction_whenSave_thenSuccess() {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(1);
        userTransaction.setOrderId(1);
        userTransaction.setAmount(1);

        UserTransaction savedUserTransaction = userTransactionRepository.saveAndFlush(userTransaction);

        Assertions.assertThat(savedUserTransaction.getId()).isNotNull();
    }

    @Test
    public void givenUserTransaction_whenSave_thenPopulateAuditFields() {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(1);
        userTransaction.setOrderId(1);
        userTransaction.setAmount(1);

        UserTransaction savedUserTransaction = userTransactionRepository.saveAndFlush(userTransaction);

        Assertions.assertThat(savedUserTransaction.getId()).isNotNull();
        Assertions.assertThat(savedUserTransaction.getCreatedAt()).isNotNull();
        Assertions.assertThat(savedUserTransaction.getUpdatedAt()).isNotNull();
    }

    @Test
    public void givenValidUserTransactionId_whenFindBy_shouldReturnUserTransaction() {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(1);
        userTransaction.setOrderId(1);
        userTransaction.setAmount(1);

        UserTransaction savedUserTransaction = userTransactionRepository.saveAndFlush(userTransaction);

        UserTransaction foundUserTransaction = userTransactionRepository.findById(savedUserTransaction.getId()).orElse(null);

        Assertions.assertThat(foundUserTransaction).isNotNull();
    }

    @Test
    public void givenInvalidTransactionId_whenFindBy_shouldThrowException() {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(1);
        userTransaction.setOrderId(1);
        userTransaction.setAmount(1);

        userTransactionRepository.saveAndFlush(userTransaction);

        Assertions.assertThatThrownBy(() -> userTransactionRepository.findById(0).orElseThrow());
    }

    @Test
    public void givenThreeOrders_whenFindAll_shouldReturnThreeOrders() {
        UserTransaction userTransaction1 = new UserTransaction();
        userTransaction1.setUserId(1);
        userTransaction1.setOrderId(1);
        userTransaction1.setAmount(1);
        UserTransaction userTransaction2 = new UserTransaction();
        userTransaction2.setUserId(1);
        userTransaction2.setOrderId(1);
        userTransaction2.setAmount(1);
        UserTransaction userTransaction3 = new UserTransaction();
        userTransaction3.setUserId(1);
        userTransaction3.setOrderId(1);
        userTransaction3.setAmount(1);

        List<UserTransaction> userTransactions = List.of(userTransaction1, userTransaction2, userTransaction3);

        userTransactionRepository.saveAllAndFlush(userTransactions);
        userTransactions = userTransactionRepository.findAll();

        Assertions.assertThat(userTransactions).isNotNull();
        Assertions.assertThat(userTransactions).hasSize(3);
    }

}
