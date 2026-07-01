package com.example.biddingservice.repository;

import com.example.biddingservice.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findByAuctionIdOrderByAmountDesc(UUID auctionId);

    @Modifying // update loại bỏ bid cũ, khi có bid mới
    @Query("UPDATE Bid b SET b.status = com.example.biddingservice.entity.BidStatus.OUTBID WHERE b.auctionId = :auctionId AND b.bidderId != :winnerId")
    void markOutbidByAuction(@Param("auctionId") UUID auctionId, @Param("winnerId") UUID winnerId);
}
