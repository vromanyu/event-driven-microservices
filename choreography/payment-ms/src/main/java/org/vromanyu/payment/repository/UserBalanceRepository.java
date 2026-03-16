package org.vromanyu.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vromanyu.payment.entity.UserBalance;

import java.util.List;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Integer> {
    List<UserBalance> findByUserId(Integer userId);
}
