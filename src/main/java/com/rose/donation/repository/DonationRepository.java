package com.rose.donation.repository;

import com.rose.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {
}
