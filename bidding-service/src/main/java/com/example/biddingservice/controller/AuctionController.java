package com.example.biddingservice.controller;

import com.example.biddingservice.dto.AuctionResponse;
import com.example.biddingservice.dto.CreateAuctionRequest;
import com.example.biddingservice.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(
            @Valid @RequestBody CreateAuctionRequest request,
            // Trong thực tế, ID này sẽ được lấy từ JWT Token do API Gateway truyền xuống
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000001") UUID sellerId
    ) {
        AuctionResponse response = auctionService.createAuction(request, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable UUID id) {
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }

    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<AuctionResponse>> getAllAuctions(
            org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(auctionService.getAllAuctions(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionResponse> updateAuction(
            @PathVariable UUID id,
            @RequestBody com.example.biddingservice.dto.UpdateAuctionRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000001") UUID sellerId
    ) {
        return ResponseEntity.ok(auctionService.updateAuction(id, request, sellerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAuction(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000001") UUID sellerId
    ) {
        auctionService.cancelAuction(id, sellerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/watches/{watchId}/price-history")
    public ResponseEntity<java.util.List<com.example.biddingservice.dto.PriceHistoryProjection>> getPriceHistory(
            @PathVariable UUID watchId) {
        return ResponseEntity.ok(auctionService.getPriceHistory(watchId));
    }
}
