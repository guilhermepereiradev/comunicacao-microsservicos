package com.br.comunicacaoms.productapi.dtos;


import com.br.comunicacaoms.productapi.model.Supplier;

public record SupplierResponse(Integer id, String name) {
    public SupplierResponse(Supplier supplier) {
        this(supplier.getId(), supplier.getName());
    }
}