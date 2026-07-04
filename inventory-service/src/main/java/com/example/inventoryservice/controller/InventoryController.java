package com.example.inventoryservice.controller;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/{watchId}/restock")
    public ResponseEntity<Inventory> restock(
            @PathVariable UUID watchId,
            @RequestParam String sku,
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.restock(watchId, sku, quantity));
    }

    @PostMapping("/{watchId}/reserve")
    public ResponseEntity<Inventory> reserveStock(
            @PathVariable UUID watchId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.reserveStock(watchId, quantity));
    }

    @PostMapping("/{watchId}/release")
    public ResponseEntity<Inventory> releaseStock(
            @PathVariable UUID watchId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.releaseStock(watchId, quantity));
    }

    @PostMapping("/{watchId}/deduct")
    public ResponseEntity<Inventory> deductStock(
            @PathVariable UUID watchId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.deductStock(watchId, quantity));
    }

    @GetMapping("/{watchId}")
    public ResponseEntity<Inventory> getInventory(@PathVariable UUID watchId) {
        return ResponseEntity.ok(inventoryService.getInventory(watchId));
    }

    @GetMapping("/{watchId}/history")
    public ResponseEntity<java.util.List<com.example.inventoryservice.entity.InventoryHistory>> getInventoryHistory(@PathVariable UUID watchId) {
        return ResponseEntity.ok(inventoryService.getInventoryHistory(watchId));
    }

    @GetMapping("/report")
    public ResponseEntity<java.util.List<com.example.inventoryservice.dto.InventoryReportDto>> getInventoryReport() {
        return ResponseEntity.ok(inventoryService.getInventoryReport());
    }
}
