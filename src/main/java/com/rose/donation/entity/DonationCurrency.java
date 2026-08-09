package com.rose.donation.entity;

public enum DonationCurrency {
    EUR;

    public String toStripeCurrency() {
        return this.name().toLowerCase();
    }
}
