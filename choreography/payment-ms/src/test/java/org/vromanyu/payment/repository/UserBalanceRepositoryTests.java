package org.vromanyu.payment.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.vromanyu.payment.entity.UserBalance;

import java.util.List;

@DataJpaTest
public class UserBalanceRepositoryTests {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Test
    public void givenUserBalance_whenSave_thenSuccess() {
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(1);
        userBalance.setPrice(100);

        UserBalance savedUserBalance = userBalanceRepository.saveAndFlush(userBalance);

        Assertions.assertThat(savedUserBalance.getId()).isNotNull();
    }

    @Test
    public void givenUserBalance_whenSave_thenPopulateAuditFields() {
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(1);
        userBalance.setPrice(100);

        UserBalance savedUserBalance = userBalanceRepository.saveAndFlush(userBalance);

        Assertions.assertThat(savedUserBalance.getId()).isNotNull();
        Assertions.assertThat(savedUserBalance.getCreatedAt()).isNotNull();
        Assertions.assertThat(savedUserBalance.getUpdatedAt()).isNotNull();
    }

    @Test
    public void givenValidUserBalanceId_whenFindBy_shouldReturnUserBalance() {
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(1);
        userBalance.setPrice(100);

        UserBalance savedUserBalance = userBalanceRepository.saveAndFlush(userBalance);

        UserBalance foundUserBalance = userBalanceRepository.findById(savedUserBalance.getId()).orElse(null);

        Assertions.assertThat(foundUserBalance).isNotNull();
    }

    @Test
    public void givenInvalidOrderId_whenFindBy_shouldThrowException() {
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(1);
        userBalance.setPrice(100);
        userBalanceRepository.saveAndFlush(userBalance);

        Assertions.assertThatThrownBy(() -> userBalanceRepository.findById(0).orElseThrow());
    }

    @Test
    public void givenThreeOrders_whenFindAll_shouldReturnThreeOrders() {
        UserBalance userBalance1 = new UserBalance();
        userBalance1.setUserId(1);
        userBalance1.setPrice(100);
        UserBalance userBalance2 = new UserBalance();
        userBalance2.setUserId(2);
        userBalance2.setPrice(100);
        UserBalance userBalance3 = new UserBalance();
        userBalance3.setUserId(3);
        userBalance3.setPrice(100);
        List<UserBalance> userBalanceList = List.of(userBalance1, userBalance2, userBalance3);

        userBalanceRepository.saveAllAndFlush(userBalanceList);
        userBalanceList = userBalanceRepository.findAll();

        Assertions.assertThat(userBalanceList).isNotNull();
        Assertions.assertThat(userBalanceList).hasSize(3);
    }

    @Test
    public void givenValidUserId_whenFindByUserId_shouldReturnUserBalance() {
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(1);
        userBalance.setPrice(100);
        userBalanceRepository.saveAndFlush(userBalance);

        List<UserBalance> userBalances = userBalanceRepository.findByUserId(1);

        Assertions.assertThat(userBalances).isNotNull();
        Assertions.assertThat(userBalances).hasSize(1);
        Assertions.assertThat(userBalances.getFirst().getUserId()).isEqualTo(1);
    }

    @Test
    public void givenInvalidUserId_whenFindByUserId_shouldReturnEmptyList() {
        List<UserBalance> userBalances = userBalanceRepository.findByUserId(0);
        Assertions.assertThat(userBalances).isNotNull();
        Assertions.assertThat(userBalances).isEmpty();
    }

}
