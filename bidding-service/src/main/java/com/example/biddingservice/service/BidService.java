package com.example.biddingservice.service;

import com.example.biddingservice.dto.BidResponse;
import com.example.biddingservice.entity.Auction;
import com.example.biddingservice.entity.AuctionStatus;
import com.example.biddingservice.entity.Bid;
import com.example.biddingservice.entity.BidStatus;
import com.example.biddingservice.exception.ResourceNotFoundException;
import com.example.biddingservice.repository.AuctionRepository;
import com.example.biddingservice.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Transactional
    public BidResponse placeBid(UUID auctionId, UUID bidderId, BigDecimal amount) {
        Auction auction = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new IllegalArgumentException("Auction is not active");
        }

        BigDecimal currentHighest = auction.getCurrentHighestBid() != null ? auction.getCurrentHighestBid()
                : auction.getStartPrice();

        // If there's already a bid, the new bid must be strictly greater.
        // If it's the very first bid, it could theoretically equal startPrice depending
        // on business logic,
        // but typically must be > current highest. Let's assume must be higher.
        if (auction.getCurrentHighestBid() != null && amount.compareTo(currentHighest) <= 0) {
            throw new IllegalArgumentException("Bid must be higher than current highest bid: " + currentHighest);
        } else if (auction.getCurrentHighestBid() == null && amount.compareTo(currentHighest) < 0) {
            throw new IllegalArgumentException("Bid must be at least the start price: " + currentHighest);
        }

        auction.setCurrentHighestBid(amount);
        auction.setHighestBidderId(bidderId); // save data (1)
        auction.setBidCount(auction.getBidCount() + 1);

        bidRepository.markOutbidByAuction(auctionId, bidderId);

        Bid bid = Bid.builder()
                .auctionId(auctionId)
                .bidderId(bidderId)
                .amount(amount)
                .status(BidStatus.ACTIVE)
                .build();
        bid = bidRepository.save(bid); // save data (2) 👇 để lấy 2 loại data gộp lại trả cho FE

        return BidResponse.from(bid, auction); // do data nhận về từ 2 nguồn nền cần tạo DTO BidResponse dùng builder để
                                               // gộp vào cho gọn.
    }
}
