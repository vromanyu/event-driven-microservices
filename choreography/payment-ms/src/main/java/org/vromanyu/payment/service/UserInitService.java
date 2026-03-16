package org.vromanyu.payment.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.payment.entity.UserBalance;
import org.vromanyu.payment.repository.UserBalanceRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserInitService implements ApplicationRunner {

    private final UserBalanceRepository userBalanceRepository;

    public UserInitService(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<UserBalance> userBalances = new ArrayList<>();
        UserBalance userBalance = new UserBalance();
        userBalance.setPrice(500);
        userBalances.add(userBalance);
        userBalance = new UserBalance();
        userBalance.setPrice(1000);
        userBalances.add(userBalance);
        userBalance = new UserBalance();
        userBalance.setPrice(2000);
        userBalances.add(userBalance);
        userBalance = new UserBalance();
        userBalance.setPrice(3000);
        userBalances.add(userBalance);
        userBalanceRepository.saveAll(userBalances);
    }
}
