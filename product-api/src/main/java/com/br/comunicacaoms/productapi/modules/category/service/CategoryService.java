package com.br.comunicacaoms.productapi.modules.category.service;

import com.br.comunicacaoms.productapi.modules.category.model.Category;
import com.br.comunicacaoms.productapi.modules.category.repository.CategoryRepository;
import com.br.comunicacaoms.productapi.configs.exceptions.BusinessRuleException;
import com.br.comunicacaoms.productapi.configs.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Transactional
    public void delete(Integer id) {
        try {
            categoryRepository.delete(findById(id));
            categoryRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessRuleException("Cannot delete category because it is in use.");
        }
    }
}
