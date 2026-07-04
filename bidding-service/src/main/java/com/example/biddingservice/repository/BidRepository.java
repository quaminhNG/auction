package com.example.biddingservice.repository;

import com.example.biddingservice.entity.Bid;
import com.example.biddingservice.dto.LeaderboardEntryProjection;
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

    @Query(value = """
        SELECT 
            b.bidder_id as bidderId,
            b.amount as amount,
            CAST(RANK() OVER (PARTITION BY b.auction_id ORDER BY b.amount DESC) AS int) AS rank,
            b.created_at as createdAt,
            LAG(b.amount) OVER (PARTITION BY b.auction_id ORDER BY b.created_at) AS prevBid,
            b.amount - LAG(b.amount) OVER (
                PARTITION BY b.auction_id ORDER BY b.created_at
            ) AS increaseAmount
        FROM bids b
        WHERE b.auction_id = :auctionId
          AND b.status = 'ACTIVE'
        ORDER BY rank
        """, nativeQuery = true)
    List<LeaderboardEntryProjection> getAuctionLeaderboard(@Param("auctionId") UUID auctionId);
}
