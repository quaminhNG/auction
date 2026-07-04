package com.example.biddingservice.repository;

import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.AuctionStatus;
import com.example.biddingservice.dto.PriceHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID> {
    List<Auction> findByStatusAndEndTimeBefore(AuctionStatus status, LocalDateTime endTime);
    List<Auction> findByStatusAndStartTimeBefore(AuctionStatus status, LocalDateTime startTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE) //khóa row
    @Query("SELECT a FROM Auction a WHERE a.id = :id")
    Optional<Auction> findByIdWithLock(@Param("id") UUID id);

    @Query(value = """
        SELECT 
            a.watch_id as watchId,
            CAST(DATE_TRUNC('month', a.end_time) AS timestamp) AS month,
            AVG(a.current_highest_bid) AS avgWinningPrice,
            MAX(a.current_highest_bid) AS maxPrice,
            MIN(a.current_highest_bid) AS minPrice,
            LAG(AVG(a.current_highest_bid)) OVER (
                PARTITION BY a.watch_id ORDER BY DATE_TRUNC('month', a.end_time)
            ) AS prevMonthAvg,
            CAST(COUNT(*) AS int) AS auctionCount
        FROM auctions a
        WHERE a.status = 'ENDED'
          AND a.watch_id = :watchId
        GROUP BY a.watch_id, DATE_TRUNC('month', a.end_time)
        ORDER BY month
        """, nativeQuery = true)
    List<PriceHistoryProjection> getPriceHistory(@Param("watchId") UUID watchId);
}
