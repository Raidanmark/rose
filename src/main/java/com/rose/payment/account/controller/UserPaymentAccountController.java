package com.rose.payment.account.controller;

import com.rose.payment.account.dto.UserOnboardingResponse;
import com.rose.payment.account.dto.UserPaymentAccountResponse;
import com.rose.payment.account.service.UserPaymentAccountService;
import com.rose.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-account")
@RequiredArgsConstructor
public class UserPaymentAccountController {

    private final UserPaymentAccountService userPaymentAccountService;

    @PostMapping("/me/onboarding")
    public UserOnboardingResponse startOnboarding(@AuthenticationPrincipal User user) {
        return userPaymentAccountService.startOnboarding(user);
    }

    @GetMapping("/me/onboarding-status")
    public UserPaymentAccountResponse getCurrentStatus(@AuthenticationPrincipal User user) {
        return userPaymentAccountService.getStatus(user);
    }
}
