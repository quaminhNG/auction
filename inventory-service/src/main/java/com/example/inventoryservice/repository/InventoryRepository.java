package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByWatchId(UUID watchId);
    Optional<Inventory> findBySku(String sku);

    @org.springframework.data.jpa.repository.Query(value = """
        WITH inventory_status AS (
            SELECT 
                watch_id,
                quantity,
                reserved,
                quantity - reserved AS available,
                CASE 
                    WHEN quantity - reserved = 0 THEN 'OUT_OF_STOCK'
                    WHEN quantity - reserved <= 3 THEN 'LOW_STOCK'
                    ELSE 'IN_STOCK'
                END AS stock_status
            FROM inventory
        ),
        stock_summary AS (
            SELECT 
                stock_status,
                COUNT(*) AS watch_count,
                SUM(available) AS total_available
            FROM inventory_status
            GROUP BY stock_status
        )
        SELECT 
            stock_status as stockStatus, 
            watch_count as watchCount, 
            total_available as totalAvailable 
        FROM stock_summary 
        ORDER BY stock_status
        """, nativeQuery = true)
    java.util.List<com.example.inventoryservice.dto.InventoryReportDto> getInventoryReport();
}
