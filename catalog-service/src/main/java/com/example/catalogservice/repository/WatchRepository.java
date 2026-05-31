package com.example.catalogservice.repository;

import com.example.catalogservice.entity.Watch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface WatchRepository extends JpaRepository<Watch, UUID> {

    @Query(value = """
            SELECT * FROM watches
            WHERE (:brandId IS NULL OR brand_id = cast(:brandId as uuid))
            AND (:minPrice IS NULL OR base_price >= :minPrice)
            AND (:maxPrice IS NULL OR base_price <= :maxPrice)
            AND (:movement IS NULL OR specifications->>'movement_type' = :movement)
            AND (:minDiam IS NULL OR (specifications->>'dial_diameter_mm')::int >= :minDiam)
            AND (:maxDiam IS NULL OR (specifications->>'dial_diameter_mm')::int <= :maxDiam)
            AND specifications @> cast(:jsonFilter as jsonb)
            """, countQuery = """
            SELECT count(*) FROM watches
            WHERE (:brandId IS NULL OR brand_id = cast(:brandId as uuid))
            AND (:minPrice IS NULL OR base_price >= :minPrice)
            AND (:maxPrice IS NULL OR base_price <= :maxPrice)
            AND (:movement IS NULL OR specifications->>'movement_type' = :movement)
            AND (:minDiam IS NULL OR (specifications->>'dial_diameter_mm')::int >= :minDiam)
            AND (:maxDiam IS NULL OR (specifications->>'dial_diameter_mm')::int <= :maxDiam)
            AND specifications @> cast(:jsonFilter as jsonb)
            """, nativeQuery = true)
    Page<Watch> searchWithSpecs(
            @Param("brandId") String brandId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("movement") String movement,
            @Param("minDiam") Integer minDiam,
            @Param("maxDiam") Integer maxDiam,
            @Param("jsonFilter") String jsonFilter,
            Pageable pageable);
}
