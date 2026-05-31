package com.example.catalogservice.service;

import com.example.catalogservice.dto.WatchSpecFilter;
import com.example.catalogservice.entity.Watch;
import com.example.catalogservice.repository.WatchRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchService {

    private final WatchRepository watchRepository;
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
            pageable
        );
    }
}
