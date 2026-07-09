package com.example.biddingservice.service;

import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.AuctionStatus;
import com.example.biddingservice.repository.AuctionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class BidServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
    }

    @Autowired
    private BidService bidService;

    @Autowired
    private AuctionRepository auctionRepository;

    @Test
    void concurrentBids_shouldOnlyAcceptHighestBid() throws InterruptedException {
        // Setup auction
        Auction auction = new Auction();
        auction.setWatchId(UUID.randomUUID());
        auction.setSellerId(UUID.randomUUID());
        auction.setStartPrice(BigDecimal.valueOf(100));
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setStartTime(LocalDateTime.now().minusDays(1));
        auction.setEndTime(LocalDateTime.now().plusDays(1));
        auction = auctionRepository.save(auction);

        // Simulate 10 concurrent bids
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        List<BigDecimal> bidAmounts = List.of(
            new BigDecimal("110"), new BigDecimal("115"), 
            new BigDecimal("112"), new BigDecimal("105"),
            new BigDecimal("118"), new BigDecimal("102"),
            new BigDecimal("120"), new BigDecimal("119"),
            new BigDecimal("108"), new BigDecimal("111")
        );

        UUID finalAuctionId = auction.getId();

        for (BigDecimal amount : bidAmounts) {
            executor.submit(() -> {
                try { 
                    bidService.placeBid(finalAuctionId, UUID.randomUUID(), amount, UUID.randomUUID().toString()); 
                } catch (Exception e) { 
                    // expected for lower bids due to concurrency
                } finally { 
                    latch.countDown(); 
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        Auction updated = auctionRepository.findById(finalAuctionId).orElseThrow();
        
        // Chỉ bid cao nhất được chấp nhận
        assertThat(updated.getCurrentHighestBid()).isEqualByComparingTo("120");
    }
}
