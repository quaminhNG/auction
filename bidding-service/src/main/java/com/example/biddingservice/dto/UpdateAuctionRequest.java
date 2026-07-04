package com.example.biddingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateAuctionRequest(
        BigDecimal startPrice,
        BigDecimal reservePrice,
        BigDecimal buyNowPrice,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
