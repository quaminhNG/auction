package com.example.biddingservice.controller;

import com.example.biddingservice.dto.BidResponse;
import com.example.biddingservice.dto.PlaceBidRequest;
import com.example.biddingservice.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auctions/{auctionId}/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable UUID auctionId,
            @Valid @RequestBody PlaceBidRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000002") UUID bidderId,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey
    ) {
        BidResponse response = bidService.placeBid(auctionId, bidderId, request.getAmount(), idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
