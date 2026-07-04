package com.example.catalogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WatchRequest(
    @NotNull(message = "Brand ID is required")
    UUID brandId,
    UUID categoryId,
    
    @NotBlank(message = "Model number is required")
    String modelNumber,
    
    @NotBlank(message = "Model name is required")
    String modelName,
    
    String description,
    
    @NotNull(message = "Base price is required")
    BigDecimal basePrice,
    
    String status,
    Map<String, Object> specifications,
    List<Map<String, Object>> images
) {
}
