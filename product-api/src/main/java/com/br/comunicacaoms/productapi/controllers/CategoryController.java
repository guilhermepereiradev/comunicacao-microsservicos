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

import java.util.ArrayList;
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
    public ResponseEntity<List<CategoryResponse>> findAll(@RequestParam(required = false) String description) {
        var categories = categoryService.findAll();
        List<CategoryResponse> categoriesResponse = categories.stream().map(CategoryResponse::new).toList();

        return ResponseEntity.ok(categoriesResponse);
    }

    @GetMapping(params = "description")
    public ResponseEntity<List<CategoryResponse>> findByDescription(@RequestParam String description) {
        var categories = categoryService.findByDescription(description);
        List<CategoryResponse> categoriesResponse = categories.stream().map(CategoryResponse::new).toList();

        return ResponseEntity.ok(categoriesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Integer id) {
        var category = categoryService.findById(id);

        return ResponseEntity.ok(new CategoryResponse(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
       categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@Valid @RequestBody CategoryRequest request, @PathVariable Integer id) {
        var category = categoryService.findById(id);
        BeanUtils.copyProperties(request, category);

        category = categoryService.save(category);

        return ResponseEntity.ok(new CategoryResponse(category));
    }
}
