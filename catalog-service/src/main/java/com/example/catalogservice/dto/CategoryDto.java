package com.example.catalogservice.dto;

import com.example.catalogservice.entity.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    private UUID id;
    private String name;
    private String slug;
    private List<CategoryDto> children = new ArrayList<>();

    public static CategoryDto from(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        return dto;
    }
}
