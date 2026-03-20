package org.vromanyu.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vromanyu.core.UserBalanceDto;
import org.vromanyu.payment.service.UserBalanceService;

import java.util.List;

@RequestMapping("/api/payments")
@RestController
public class UserBalanceController {

    private final UserBalanceService userBalanceService;

    public UserBalanceController(UserBalanceService userBalanceService) {
        this.userBalanceService = userBalanceService;
    }

    @GetMapping("/all-balances")
    public ResponseEntity<List<UserBalanceDto>> getAllUserBalances() {
        return ResponseEntity.ok().body(userBalanceService.getAllUsersWithBalance());
    }

}
