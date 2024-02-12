package com.br.comunicacaoms.productapi.controllers;

import com.br.comunicacaoms.productapi.dtos.SupplierRequest;
import com.br.comunicacaoms.productapi.dtos.SupplierResponse;
import com.br.comunicacaoms.productapi.model.Supplier;
import com.br.comunicacaoms.productapi.services.SupplierService;
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
}
