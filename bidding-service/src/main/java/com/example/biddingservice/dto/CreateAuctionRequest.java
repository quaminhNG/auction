package com.example.biddingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAuctionRequest(
        @NotNull(message = "Watch ID is required")
        UUID watchId,
        
        @NotNull(message = "Start price is required")
        @Positive(message = "Start price must be positive")
        BigDecimal startPrice,
        
        BigDecimal reservePrice,
        
        BigDecimal buyNowPrice,
        
        @NotNull(message = "Start time is required")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,
        
        @NotNull(message = "End time is required")
        @Future(message = "End time must be in the future")
        LocalDateTime endTime
) {}
