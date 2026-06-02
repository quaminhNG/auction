package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(name = "watch_id", nullable = false)
    private UUID watchId;

    @Column(name = "change_type", nullable = false, length = 30)
    private String changeType;

    @Column(name = "quantity_before", nullable = false)
    private Integer quantityBefore;

    @Column(name = "quantity_after", nullable = false)
    private Integer quantityAfter;

    @Column(name = "reserved_before", nullable = false)
    private Integer reservedBefore;

    @Column(name = "reserved_after", nullable = false)
    private Integer reservedAfter;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
