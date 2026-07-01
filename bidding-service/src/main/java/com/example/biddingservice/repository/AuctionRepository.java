package com.example.biddingservice.repository;

import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.AuctionStatus;
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

    @Lock(LockModeType.PESSIMISTIC_WRITE) //khóa row
    @Query("SELECT a FROM Auction a WHERE a.id = :id")
    Optional<Auction> findByIdWithLock(@Param("id") UUID id);
}
