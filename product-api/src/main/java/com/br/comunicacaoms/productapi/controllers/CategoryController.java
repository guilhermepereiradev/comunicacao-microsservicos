package com.br.comunicacaoms.productapi.controllers;

import com.br.comunicacaoms.productapi.dtos.CategoryRequest;
import com.br.comunicacaoms.productapi.dtos.CategoryResponse;
import com.br.comunicacaoms.productapi.model.Category;
import com.br.comunicacaoms.productapi.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    public ResponseEntity<CategoryResponse> save(@Valid @RequestBody CategoryRequest request) {
        var category = new Category();
        BeanUtils.copyProperties(request, category);
        category = categoryService.save(category);

        return ResponseEntity.ok(new CategoryResponse(category));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        var categories = categoryService.findAll();
        List<CategoryResponse> categoriesResponse = categories.stream().map(CategoryResponse::new).toList();

        return ResponseEntity.ok(categoriesResponse);
    }
}
