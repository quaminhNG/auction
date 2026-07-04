package com.example.biddingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface LeaderboardEntryProjection {
    UUID getBidderId();
    BigDecimal getAmount();
    Integer getRank();
    LocalDateTime getCreatedAt();
    BigDecimal getPrevBid();
    BigDecimal getIncreaseAmount();
}
