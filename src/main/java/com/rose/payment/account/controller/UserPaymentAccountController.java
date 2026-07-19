//package com.rose.payment.account.controller;
//
//import com.rose.payment.account.service.UserPaymentAccountService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/user-payment-account")
//@RequiredArgsConstructor
//public class UserPaymentAccountController {
//
//    private final UserPaymentAccountService userPaymentAccountService;
//
//    @PostMapping("/onboarding")
//    public UserOnboardingResponse startOnboarding(@AuthenticationPrincipal User user) {
//        return userPaymentAccountService.startOnboarding(user);
//    }
//
//    @GetMapping("/me")
//    public UserPaymentAccountResponse getCurrentStatus(@AuthenticationPrincipal User user) {
//        return userPaymentAccountService.getCurrentStatus(user.getId());
//    }
//}
