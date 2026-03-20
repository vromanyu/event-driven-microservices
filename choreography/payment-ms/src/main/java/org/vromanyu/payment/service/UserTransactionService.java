package org.vromanyu.payment.service;

import org.vromanyu.core.UserTransactionDto;

import java.util.List;

public interface UserTransactionService {
    List<UserTransactionDto> getAllUserTransactions();
}
