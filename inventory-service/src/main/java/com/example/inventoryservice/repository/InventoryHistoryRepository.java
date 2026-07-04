package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, UUID> {
    List<InventoryHistory> findByWatchIdOrderByCreatedAtDesc(UUID watchId);
}
