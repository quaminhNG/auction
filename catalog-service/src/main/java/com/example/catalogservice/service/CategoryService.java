package com.example.catalogservice.service;

import com.example.catalogservice.dto.CategoryDto;
import com.example.catalogservice.entity.Category;
import com.example.catalogservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();
        
        // Map from Category ID to CategoryDto
        Map<UUID, CategoryDto> dtoMap = allCategories.stream()
            .collect(Collectors.toMap(Category::getId, CategoryDto::from));
            
        List<CategoryDto> rootCategories = new java.util.ArrayList<>();
        
        for (Category category : allCategories) {
            CategoryDto dto = dtoMap.get(category.getId());
            if (category.getParent() == null) {
                rootCategories.add(dto);
            } else {
                CategoryDto parentDto = dtoMap.get(category.getParent().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            }
        }
        
        return rootCategories;
    }
}
