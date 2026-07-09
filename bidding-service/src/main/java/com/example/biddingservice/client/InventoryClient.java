package com.example.biddingservice.client;

import com.example.biddingservice.dto.InventoryResponse;
import com.example.biddingservice.dto.ReserveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "inventory-service", url = "${services.inventory.url}")
public interface InventoryClient {

    @PostMapping("/api/inventory/{watchId}/reserve")
    InventoryResponse reserveStock(
        @PathVariable("watchId") UUID watchId,
        @RequestBody ReserveRequest request
    );

    @GetMapping("/api/inventory/{watchId}")
    InventoryResponse getInventory(@PathVariable("watchId") UUID watchId);
}
