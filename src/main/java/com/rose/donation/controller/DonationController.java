package com.rose.donation.controller;

import com.rose.donation.dto.CreateDonationRequest;
import com.rose.donation.dto.CreateDonationResponse;
import com.rose.donation.dto.DonationResponse;
import com.rose.donation.service.DonationPaymentService;
import com.rose.donation.service.DonationService;
import com.rose.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import java.util.UUID;

@RestController
@RequestMapping("/donation")
@RequiredArgsConstructor
public class DonationController {

    private static final int MAX_PAGE_SIZE = 50;

    private final DonationPaymentService donationPaymentService;
    private final DonationService donationService;

    @PostMapping
    public CreateDonationResponse createDonation(
            @AuthenticationPrincipal User sender,
            @Valid @RequestBody CreateDonationRequest createDonationRequest
    ) {
        return donationPaymentService.createDonation(sender, createDonationRequest);
    }

    @GetMapping("/{donationId}")
    public DonationResponse getDonation(
            @AuthenticationPrincipal User user,
            @PathVariable UUID donationId
    ) {
        return donationService.getDonation(donationId, user.getId());
    }

    @GetMapping("/sent")
    public Page<DonationResponse> getDonation(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return donationService.getSentDonations(
                user.getId(),
                createPageable(page, size)
        );
    }

    @GetMapping("/received")
    public Page<DonationResponse> getReceivedDonations(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return donationService.getReceivedDonations(
                user.getId(),
                createPageable(page, size)
        );
    }

    private Pageable createPageable(int page, int size) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.clamp(size, 1, MAX_PAGE_SIZE);

        return PageRequest.of(
                safePage,
                safeSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
    }
}
