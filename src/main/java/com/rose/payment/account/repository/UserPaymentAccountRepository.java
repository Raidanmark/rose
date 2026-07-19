package com.rose.payment.account.repository;


import com.rose.payment.account.entity.UserPaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPaymentAccountRepository extends JpaRepository<UserPaymentAccount, UUID> {
    Optional<UserPaymentAccount> findByUserId(UUID userId);
}
