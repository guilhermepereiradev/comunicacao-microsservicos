package com.br.comunicacaoms.productapi.modules.supplier.controller;

import com.br.comunicacaoms.productapi.modules.supplier.dto.SupplierRequest;
import com.br.comunicacaoms.productapi.modules.supplier.dto.SupplierResponse;
import com.br.comunicacaoms.productapi.modules.supplier.model.Supplier;
import com.br.comunicacaoms.productapi.modules.supplier.service.SupplierService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@AllArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponse> save(@Valid @RequestBody SupplierRequest request) {
        var supplier = new Supplier();
        BeanUtils.copyProperties(request, supplier);

        supplier = supplierService.save(supplier);

        return ResponseEntity.ok(new SupplierResponse(supplier));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> findAll() {
        var suppliers = supplierService.findAll();
        List<SupplierResponse> suppliersResponse = suppliers.stream().map(SupplierResponse::new).toList();

        return ResponseEntity.ok(suppliersResponse);
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<SupplierResponse>> findAByName(@RequestParam String name) {
        var suppliers = supplierService.findByName(name);
        List<SupplierResponse> suppliersResponse = suppliers.stream().map(SupplierResponse::new).toList();

        return ResponseEntity.ok(suppliersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> findById(@PathVariable Integer id) {
        var supplier = supplierService.findById(id);

        return ResponseEntity.ok(new SupplierResponse(supplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        supplierService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> update(@Valid @RequestBody SupplierRequest request, @PathVariable Integer id) {
        var supplier = supplierService.findById(id);
        BeanUtils.copyProperties(request, supplier);

        supplier = supplierService.save(supplier);
        return ResponseEntity.ok(new SupplierResponse(supplier));
    }
}
