package org.vromanyu.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.UserBalanceDto;
import org.vromanyu.payment.repository.UserBalanceRepository;

import java.util.List;

@Service
public class UserBalanceServiceImpl implements UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;

    public UserBalanceServiceImpl(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBalanceDto> getAllUsersWithBalance() {
        return userBalanceRepository.findAll().stream().map(balance -> new UserBalanceDto(balance.getUserId(), balance.getPrice())).toList();
    }
}
