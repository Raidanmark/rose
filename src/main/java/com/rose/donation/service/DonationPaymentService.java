package com.rose.donation.service;

import com.rose.donation.exception.SelfDonationNotAllowedException;
import com.rose.donation.dto.CreateDonationRequest;
import com.rose.donation.dto.CreateDonationResponse;
import com.rose.donation.entity.Donation;
import com.rose.donation.repository.DonationRepository;
import com.rose.payment.account.entity.UserPaymentAccount;
import com.rose.payment.account.service.UserPaymentAccountService;
import com.rose.payment.processing.PaymentGateway;
import com.rose.payment.processing.PaymentResult;
import com.rose.user.entity.User;
import com.rose.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationPaymentService {

    private final UserService userService;
    private final UserPaymentAccountService paymentAccountService;
    private final DonationRepository donationRepository;
    private final PaymentGateway paymentGateway;
    private final DonationPersistenceService donationPersistenceService;

    public CreateDonationResponse createDonation(
            User sender,
            CreateDonationRequest createDonationRequest
    ) {
        User recipient = userService.findUserById(createDonationRequest.recipientId());

        validateNotSelfDonation(sender, recipient);

        UserPaymentAccount paymentAccount = paymentAccountService.requireActiveAccount(recipient.getId());

        Donation donation = new Donation(
                sender,
                recipient,
                createDonationRequest.amount(),
                createDonationRequest.currency(),
                createDonationRequest.message()
        );

        donation = donationPersistenceService.save(donation);

        PaymentResult paymentResult = paymentGateway.createPaymentIntent(
                donation,
                paymentAccount.getProviderAccountId()
        );

        donationPersistenceService.attachProviderPayment(
                donation.getId(),
                paymentResult.paymentIntentId()
        );

        return new CreateDonationResponse(
                donation.getId(),
                donation.getStatus(),
                paymentResult.clientSecret()
        );
    }

    private void validateNotSelfDonation(User sender, User recipient) {
        if (sender.getId().equals(recipient.getId())) {
            throw new SelfDonationNotAllowedException();
        }
    }
}
