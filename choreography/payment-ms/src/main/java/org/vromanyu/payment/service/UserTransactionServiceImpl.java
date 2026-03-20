package org.vromanyu.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vromanyu.core.UserTransactionDto;
import org.vromanyu.payment.repository.UserTransactionRepository;

import java.util.List;

@Service
public class UserTransactionServiceImpl implements UserTransactionService {

    private final UserTransactionRepository userTransactionRepository;

    public UserTransactionServiceImpl(UserTransactionRepository userTransactionRepository) {
        this.userTransactionRepository = userTransactionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTransactionDto> getAllUserTransactions() {
        return userTransactionRepository.findAll().stream()
                .map(transaction -> new UserTransactionDto(transaction.getId(), transaction.getOrderId(), transaction.getAmount()))
                .toList();
    }

}
