package com.br.comunicacaoms.productapi.modules.product.controller;

import com.br.comunicacaoms.productapi.modules.product.dto.ProductRequest;
import com.br.comunicacaoms.productapi.modules.product.dto.ProductResponse;
import com.br.comunicacaoms.productapi.modules.product.model.Product;
import com.br.comunicacaoms.productapi.modules.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest request) {
        var product = new Product();
        product.setName(request.name());
        product.setQuantityAvailable(request.quantityAvailable());

        product = productService.save(product, request.categoryId(), request.supplierId());

        return ResponseEntity.ok(new ProductResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        var products = productService.findAll();
        List<ProductResponse> productsResponse = products.stream().map(ProductResponse::new).toList();

        return ResponseEntity.ok(productsResponse);
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<List<ProductResponse>> findByCategoryId(@RequestParam Integer categoryId) {
        var products = productService.findByCategoryId(categoryId);
        List<ProductResponse> productsResponse = products.stream().map(ProductResponse::new).toList();

        return ResponseEntity.ok(productsResponse);
    }

    @GetMapping(params = "supplierId")
    public ResponseEntity<List<ProductResponse>> findBySupplierId(@RequestParam Integer supplierId) {
        var products = productService.findBySupplierId(supplierId);
        List<ProductResponse> productsResponse = products.stream().map(ProductResponse::new).toList();

        return ResponseEntity.ok(productsResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Integer id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(new ProductResponse(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteById(@PathVariable Integer id) {
        productService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductRequest request, @PathVariable Integer id) {
        var product = productService.findById(id);
        product.setName(request.name());
        product.setQuantityAvailable(request.quantityAvailable());

        product = productService.save(product, request.categoryId(), request.supplierId());

        return ResponseEntity.ok(new ProductResponse(product));
    }
}
