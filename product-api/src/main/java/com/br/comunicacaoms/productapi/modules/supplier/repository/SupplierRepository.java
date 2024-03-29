package com.br.comunicacaoms.productapi.modules.supplier.repository;

import com.br.comunicacaoms.productapi.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository <Supplier, Integer> {

    List<Supplier> findByNameContainingIgnoreCase(String name);
}
