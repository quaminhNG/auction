package com.example.biddingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PriceHistoryProjection {
    UUID getWatchId();
    LocalDateTime getMonth();
    BigDecimal getAvgWinningPrice();
    BigDecimal getMaxPrice();
    BigDecimal getMinPrice();
    BigDecimal getPrevMonthAvg();
    Integer getAuctionCount();
}
