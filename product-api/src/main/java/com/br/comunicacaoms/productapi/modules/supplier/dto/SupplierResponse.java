package com.br.comunicacaoms.productapi.modules.supplier.dto;


import com.br.comunicacaoms.productapi.modules.supplier.model.Supplier;

public record SupplierResponse(Integer id, String name) {
    public SupplierResponse(Supplier supplier) {
        this(supplier.getId(), supplier.getName());
    }
}