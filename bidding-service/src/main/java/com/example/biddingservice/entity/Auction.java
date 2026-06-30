package com.example.biddingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "watch_id", nullable = false)
    private UUID watchId;
    
    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;
    
    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;
    
    @Column(name = "reserve_price")
    private BigDecimal reservePrice;
    
    @Column(name = "buy_now_price")
    private BigDecimal buyNowPrice;
    
    @Column(name = "current_highest_bid")
    private BigDecimal currentHighestBid;
    
    @Column(name = "highest_bidder_id")
    private UUID highestBidderId;
    
    @Column(name = "bid_count")
    @Builder.Default
    private Integer bidCount = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuctionStatus status;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Version
    private Integer version;
}
