package com.example.catalogservice.controller;

import com.example.catalogservice.dto.WatchRequest;
import com.example.catalogservice.dto.WatchSpecFilter;
import com.example.catalogservice.entity.Watch;
import com.example.catalogservice.service.WatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/watches")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    @GetMapping
    public ResponseEntity<Page<Watch>> getAllWatches(Pageable pageable) {
        return ResponseEntity.ok(watchService.getAllWatches(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Watch> getWatchById(@PathVariable UUID id) {
        return ResponseEntity.ok(watchService.getWatchById(id));
    }

    @PostMapping
    public ResponseEntity<Watch> createWatch(@Valid @RequestBody WatchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(watchService.createWatch(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Watch> updateWatch(@PathVariable UUID id, @Valid @RequestBody WatchRequest request) {
        return ResponseEntity.ok(watchService.updateWatch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatch(@PathVariable UUID id) {
        watchService.deleteWatch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Watch>> searchWatches(WatchSpecFilter filter, Pageable pageable) {
        Page<Watch> result = watchService.searchWatches(filter, pageable);
        return ResponseEntity.ok(result);
    }
}
