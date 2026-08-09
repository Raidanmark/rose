package com.rose.common.exception.donation;

public class SelfDonationNotAllowedException extends RuntimeException {
    public SelfDonationNotAllowedException() {
        super("You cannot donate to yourself");
    }
}
