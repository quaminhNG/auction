package com.example.biddingservice.repository;

import com.example.biddingservice.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findByAuctionIdOrderByAmountDesc(UUID auctionId);
}
