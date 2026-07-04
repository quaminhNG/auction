package com.example.biddingservice.scheduler;

import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.AuctionStatus;
import com.example.biddingservice.repository.AuctionRepository;
import com.example.biddingservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionScheduler {

    private final AuctionRepository auctionRepository;
    private final AuctionService auctionService;

    // Run every 10 seconds to activate scheduled auctions
    @Scheduled(fixedRateString = "${auction.scheduler.rate:10000}")
    public void activateScheduledAuctions() {
        List<Auction> scheduledAuctions = auctionRepository.findByStatusAndStartTimeBefore(AuctionStatus.SCHEDULED, LocalDateTime.now());
        for (Auction auction : scheduledAuctions) {
            try {
                auctionService.activateAuction(auction.getId());
            } catch (Exception e) {
                log.error("Failed to activate auction {}", auction.getId(), e);
            }
        }
    }

    // Run every 10 seconds to close expired auctions
    @Scheduled(fixedRateString = "${auction.scheduler.rate:10000}")
    public void closeExpiredAuctions() {
        List<Auction> expiredAuctions = auctionRepository.findByStatusAndEndTimeBefore(AuctionStatus.ACTIVE, LocalDateTime.now());
        for (Auction auction : expiredAuctions) {
            try {
                auctionService.closeAuction(auction.getId());
            } catch (Exception e) {
                log.error("Failed to close auction {}", auction.getId(), e);
            }
        }
    }
}
