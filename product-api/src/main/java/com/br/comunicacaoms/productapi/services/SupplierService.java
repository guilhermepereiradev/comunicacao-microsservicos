package com.br.comunicacaoms.productapi.services;

import com.br.comunicacaoms.productapi.model.Category;
import com.br.comunicacaoms.productapi.model.Supplier;
import com.br.comunicacaoms.productapi.repositories.SupplierRepository;
import com.br.comunicacaoms.productapi.services.exceptions.BusinessRuleException;
import com.br.comunicacaoms.productapi.services.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Transactional
    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public List<Supplier> findByName(String description) {
        return supplierRepository.findByNameContainingIgnoreCase(description);
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Supplier findById(Integer id) {
        return supplierRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Supplier not found for id: " + id));
    }

    @Transactional
    public void delete(Integer id) {
        try {
            supplierRepository.delete(findById(id));
            supplierRepository.flush();
        } catch (DataIntegrityViolationException e){
            throw new BusinessRuleException("Cannot delete supplier because it is in use.");
        }
    }
}