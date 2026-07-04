package com.example.inventoryservice.service;

import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final com.example.inventoryservice.repository.InventoryHistoryRepository inventoryHistoryRepository;

    @Transactional
    public Inventory restock(UUID watchId, String sku, int quantityToAdd) {
        Inventory inventory = inventoryRepository.findByWatchId(watchId)
            .orElseGet(() -> Inventory.builder()
                .watchId(watchId)
                .sku(sku)
                .quantity(0)
                .reserved(0)
                .build());

        inventory.addQuantity(quantityToAdd);
        
        // Note: The database trigger 'log_inventory_change' will automatically
        // create an entry in 'inventory_history' when this is saved.
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory reserveStock(UUID watchId, int quantityToReserve) {
        Inventory inventory = inventoryRepository.findByWatchId(watchId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for watchId: " + watchId));

        inventory.reserve(quantityToReserve);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory releaseStock(UUID watchId, int quantityToRelease) {
        Inventory inventory = inventoryRepository.findByWatchId(watchId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for watchId: " + watchId));

        inventory.release(quantityToRelease);
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory deductStock(UUID watchId, int quantityToDeduct) {
        Inventory inventory = inventoryRepository.findByWatchId(watchId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for watchId: " + watchId));

        inventory.deduct(quantityToDeduct);
        return inventoryRepository.save(inventory);
    }
    @Transactional(readOnly = true)
    public Inventory getInventory(UUID watchId) {
        return inventoryRepository.findByWatchId(watchId)
            .orElseThrow(() -> new RuntimeException("Inventory not found for watchId: " + watchId));
    }

    @Transactional(readOnly = true)
    public java.util.List<com.example.inventoryservice.entity.InventoryHistory> getInventoryHistory(UUID watchId) {
        return inventoryHistoryRepository.findByWatchIdOrderByCreatedAtDesc(watchId);
    }

    @Transactional(readOnly = true)
    public java.util.List<com.example.inventoryservice.dto.InventoryReportDto> getInventoryReport() {
        return inventoryRepository.getInventoryReport();
    }
}
