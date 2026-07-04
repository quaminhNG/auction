package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "watch_id", nullable = false, unique = true)
    private UUID watchId;

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer reserved = 0;

    @Version
    @Column(nullable = false)
    @Builder.Default
    private Integer version = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addQuantity(int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity to add must be positive");
        }
        this.quantity += quantityToAdd;
    }

    public void reserve(int quantityToReserve) {
        if (quantityToReserve <= 0) {
            throw new IllegalArgumentException("Quantity to reserve must be positive");
        }
        int available = this.quantity - this.reserved;
        if (available < quantityToReserve) {
            throw new RuntimeException("Not enough available stock to reserve. Available: " + available + ", Requested: " + quantityToReserve);
        }
        this.reserved += quantityToReserve;
    }

    public void release(int quantityToRelease) {
        if (quantityToRelease <= 0) {
            throw new IllegalArgumentException("Quantity to release must be positive");
        }
        if (this.reserved < quantityToRelease) {
            throw new RuntimeException("Cannot release more than reserved stock. Reserved: " + this.reserved + ", Release requested: " + quantityToRelease);
        }
        this.reserved -= quantityToRelease;
    }

    public void deduct(int quantityToDeduct) {
        if (quantityToDeduct <= 0) {
            throw new IllegalArgumentException("Quantity to deduct must be positive");
        }
        if (this.reserved < quantityToDeduct) {
            throw new RuntimeException("Cannot deduct stock that hasn't been reserved first");
        }
        if (this.quantity < quantityToDeduct) {
            throw new RuntimeException("Cannot deduct more than total quantity");
        }
        this.quantity -= quantityToDeduct;
        this.reserved -= quantityToDeduct;
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
