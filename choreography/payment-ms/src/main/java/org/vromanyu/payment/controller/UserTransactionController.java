package org.vromanyu.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vromanyu.core.UserTransactionDto;
import org.vromanyu.payment.service.UserTransactionService;

import java.util.List;

@RequestMapping("/api/payments")
@RestController
public class UserTransactionController {

    private final UserTransactionService userTransactionService;

    public UserTransactionController(UserTransactionService userTransactionService) {
        this.userTransactionService = userTransactionService;
    }

    @GetMapping(value = "/all-transactions")
    public ResponseEntity<List<UserTransactionDto>> getAllUserTransactions() {
        return ResponseEntity.ok().body(userTransactionService.getAllUserTransactions());
    }
    
}
