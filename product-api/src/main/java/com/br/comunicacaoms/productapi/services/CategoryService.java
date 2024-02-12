package com.br.comunicacaoms.productapi.services;

import com.br.comunicacaoms.productapi.model.Category;
import com.br.comunicacaoms.productapi.repositories.CategoryRepository;
import com.br.comunicacaoms.productapi.services.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findByDescription(String description) {
        return categoryRepository.findByDescriptionContainingIgnoreCase(description);
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category not found for id: " + id));
    }
}
