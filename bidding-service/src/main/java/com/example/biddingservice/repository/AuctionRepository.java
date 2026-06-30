package com.example.biddingservice.repository;

import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    List<Auction> findByStatusAndEndTimeBefore(AuctionStatus status, LocalDateTime endTime);
}
