package com.example.biddingservice.dto;

import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.Bid;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BidResponse {
    private UUID bidId;
    private UUID auctionId;
    private UUID bidderId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    
    private BigDecimal currentHighestBid;
    
    public static BidResponse from(Bid bid, Auction auction) {
        return BidResponse.builder()
                .bidId(bid.getId())
                .auctionId(bid.getAuctionId())
                .bidderId(bid.getBidderId())
                .amount(bid.getAmount())
                .status(bid.getStatus().name())
                .createdAt(bid.getCreatedAt())
                .currentHighestBid(auction.getCurrentHighestBid())
                .build();
    }
}
