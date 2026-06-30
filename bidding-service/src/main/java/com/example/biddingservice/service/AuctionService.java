package com.example.biddingservice.service;

import com.example.biddingservice.dto.AuctionResponse;
import com.example.biddingservice.dto.CreateAuctionRequest;
import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.AuctionStatus;
import com.example.biddingservice.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;

    @Transactional
    public AuctionResponse createAuction(CreateAuctionRequest request, UUID sellerId) {
        log.info("Creating auction for watch {} by seller {}", request.watchId(), sellerId);

        // 1. Business Validation
        if (request.endTime().isBefore(request.startTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        if (request.reservePrice() != null && request.reservePrice().compareTo(request.startPrice()) < 0) {
            throw new IllegalArgumentException("Reserve price cannot be lower than start price");
        }

        // 2. Initialize Auction Entity
        Auction auction = Auction.builder()
                .watchId(request.watchId())
                .sellerId(sellerId) // Lấy từ header/token
                .startPrice(request.startPrice())
                .reservePrice(request.reservePrice())
                .buyNowPrice(request.buyNowPrice())
                .status(AuctionStatus.SCHEDULED) // Trạng thái ban đầu: Đã lên lịch
                .startTime(request.startTime())
                .endTime(request.endTime())
                .bidCount(0)
                .build();

        // 3. Save to DB
        Auction savedAuction = auctionRepository.save(auction);
        
        return AuctionResponse.from(savedAuction);
    }

    @Transactional(readOnly = true)
    public AuctionResponse getAuctionById(UUID id) {
        log.info("Fetching auction details for id: {}", id);
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new com.example.biddingservice.exception.ResourceNotFoundException("Auction not found with id: " + id));
        return AuctionResponse.from(auction);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<AuctionResponse> getAllAuctions(org.springframework.data.domain.Pageable pageable) {
        log.info("Fetching all auctions");
        return auctionRepository.findAll(pageable).map(AuctionResponse::from);
    }

    @Transactional
    public AuctionResponse updateAuction(UUID id, com.example.biddingservice.dto.UpdateAuctionRequest request, UUID sellerId) {
        log.info("Updating auction {}", id);
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new com.example.biddingservice.exception.ResourceNotFoundException("Auction not found"));

        if (!auction.getSellerId().equals(sellerId)) {
            throw new IllegalArgumentException("Only seller can update this auction");
        }

        if (auction.getStatus() != AuctionStatus.DRAFT && auction.getStatus() != AuctionStatus.SCHEDULED) {
            throw new IllegalArgumentException("Cannot update active or ended auction");
        }

        if (request.startTime() != null) auction.setStartTime(request.startTime());
        if (request.endTime() != null) auction.setEndTime(request.endTime());
        if (request.startPrice() != null) auction.setStartPrice(request.startPrice());
        if (request.reservePrice() != null) auction.setReservePrice(request.reservePrice());
        if (request.buyNowPrice() != null) auction.setBuyNowPrice(request.buyNowPrice());

        if (auction.getEndTime().isBefore(auction.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        Auction updated = auctionRepository.save(auction);
        return AuctionResponse.from(updated);
    }

    @Transactional
    public void cancelAuction(UUID id, UUID sellerId) {
        log.info("Cancelling auction {}", id);
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new com.example.biddingservice.exception.ResourceNotFoundException("Auction not found"));

        if (!auction.getSellerId().equals(sellerId)) {
            throw new IllegalArgumentException("Only seller can cancel this auction");
        }

        if (auction.getStatus() == AuctionStatus.ENDED || auction.getStatus() == AuctionStatus.CANCELLED) {
            throw new IllegalArgumentException("Auction is already ended or cancelled");
        }

        auction.setStatus(AuctionStatus.CANCELLED);
        auctionRepository.save(auction);
    }
}
