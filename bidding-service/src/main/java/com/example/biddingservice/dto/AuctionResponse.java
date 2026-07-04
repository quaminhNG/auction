package com.example.biddingservice.dto;

import com.example.biddingservice.entity.Auction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AuctionResponse(
        UUID id,
        UUID watchId,
        UUID sellerId,
        BigDecimal startPrice,
        BigDecimal reservePrice,
        BigDecimal buyNowPrice,
        BigDecimal currentHighestBid,
        UUID highestBidderId,
        Integer bidCount,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static AuctionResponse from(Auction auction) {
        return new AuctionResponse(
                auction.getId(),
                auction.getWatchId(),
                auction.getSellerId(),
                auction.getStartPrice(),
                auction.getReservePrice(),
                auction.getBuyNowPrice(),
                auction.getCurrentHighestBid(),
                auction.getHighestBidderId(),
                auction.getBidCount(),
                auction.getStatus().name(),
                auction.getStartTime(),
                auction.getEndTime()
        );
    }
}
