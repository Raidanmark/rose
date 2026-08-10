package com.rose.donation.exception;

public class SelfDonationNotAllowedException extends RuntimeException {
    public SelfDonationNotAllowedException() {
        super("You cannot donate to yourself");
    }
}
