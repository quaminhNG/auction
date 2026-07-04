package com.example.inventoryservice.dto;

public interface InventoryReportDto {
    String getStockStatus();
    Long getWatchCount();
    Long getTotalAvailable();
}
