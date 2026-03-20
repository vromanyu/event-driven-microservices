package org.vromanyu.payment.service;

import org.vromanyu.core.UserBalanceDto;

import java.util.List;

public interface UserBalanceService {
    List<UserBalanceDto> getAllUsersWithBalance();
}
