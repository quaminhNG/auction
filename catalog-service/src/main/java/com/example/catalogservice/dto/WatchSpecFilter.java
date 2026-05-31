package com.example.catalogservice.dto;

import java.math.BigDecimal;

public record WatchSpecFilter(
    String brand,
    String movement_type,
    Integer min_diameter,
    Integer max_diameter,
    String glass,
    String water_resistance_label,
    BigDecimal minPrice,
    BigDecimal maxPrice
) {
    /**
     * Domain Logic
     */
    public String toJsonFilter(com.fasterxml.jackson.databind.ObjectMapper mapper) {
        java.util.Map<String, Object> jsonMap = new java.util.HashMap<>();
        
        if (this.glass != null) {
            jsonMap.put("glass", this.glass);
        }
        if (this.water_resistance_label != null) {
            jsonMap.put("water_resistance_label", this.water_resistance_label); 
        }

        try {
            return jsonMap.isEmpty() ? "{}" : mapper.writeValueAsString(jsonMap);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            return "{}"; 
        }
    }
}
