package com.example.catalogservice.service;

import com.example.catalogservice.dto.WatchRequest;
import com.example.catalogservice.dto.WatchSpecFilter;
import com.example.catalogservice.entity.Brand;
import com.example.catalogservice.entity.Category;
import com.example.catalogservice.entity.Watch;
import com.example.catalogservice.repository.BrandRepository;
import com.example.catalogservice.repository.CategoryRepository;
import com.example.catalogservice.repository.WatchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchService {

    private final WatchRepository watchRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public Page<Watch> searchWatches(WatchSpecFilter filter, Pageable pageable) {
        String jsonFilterString = filter.toJsonFilter(objectMapper);

        return watchRepository.searchWithSpecs(
                filter.brand(),
                filter.minPrice(),
                filter.maxPrice(),
                filter.movement_type(),
                filter.min_diameter(),
                filter.max_diameter(),
                jsonFilterString,
                pageable);
    }

    public Page<Watch> getAllWatches(Pageable pageable) {
        return watchRepository.findAll(pageable);
    }

    public Watch getWatchById(UUID id) {
        return watchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watch not found with id: " + id));
    }

    public Watch createWatch(WatchRequest request) {
        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        Watch watch = Watch.builder()
                .brand(brand)
                .category(category)
                .modelNumber(request.modelNumber())
                .modelName(request.modelName())
                .description(request.description())
                .basePrice(request.basePrice())
                .status(request.status() != null ? request.status() : "ACTIVE")
                .specifications(request.specifications() != null ? request.specifications() : java.util.Map.of())
                .images(request.images() != null ? request.images() : java.util.List.of())
                .build();

        return watchRepository.save(watch);
    }

    public Watch updateWatch(UUID id, WatchRequest request) {
        Watch watch = getWatchById(id);

        if (request.brandId() != null) {
            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found"));
            watch.setBrand(brand);
        }

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            watch.setCategory(category);
        }

        if (request.modelNumber() != null)
            watch.setModelNumber(request.modelNumber());
        if (request.modelName() != null)
            watch.setModelName(request.modelName());
        if (request.description() != null)
            watch.setDescription(request.description());
        if (request.basePrice() != null)
            watch.setBasePrice(request.basePrice());
        if (request.status() != null)
            watch.setStatus(request.status());
        if (request.specifications() != null)
            watch.setSpecifications(request.specifications());
        if (request.images() != null)
            watch.setImages(request.images());

        return watchRepository.save(watch);
    }

    public void deleteWatch(UUID id) {
        if (!watchRepository.existsById(id)) {
            throw new RuntimeException("Watch not found with id: " + id);
        }
        watchRepository.deleteById(id);
    }
}
