package com.example.catalogservice.controller;

import com.example.catalogservice.dto.WatchSpecFilter;
import com.example.catalogservice.entity.Watch;
import com.example.catalogservice.service.WatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/watches")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    /**
     * API: GET /api/watches/search?brand=...&glass=sapphire&page=0&size=10
     * Spring Boot tự động map query parameters trên URL vào record WatchSpecFilter
     * và tự động map tham số phân trang vào Pageable.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Watch>> searchWatches(WatchSpecFilter filter, Pageable pageable) {
        Page<Watch> result = watchService.searchWatches(filter, pageable);
        return ResponseEntity.ok(result);
    }
}
