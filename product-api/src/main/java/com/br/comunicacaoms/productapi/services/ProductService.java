package com.br.comunicacaoms.productapi.services;

import com.br.comunicacaoms.productapi.model.Product;
import com.br.comunicacaoms.productapi.repositories.ProductRepository;
import com.br.comunicacaoms.productapi.services.exceptions.BusinessRuleException;
import com.br.comunicacaoms.productapi.services.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    @Transactional
    public Product save(Product product, Integer categoryId, Integer supplierId) {
        try {
            var category = categoryService.findById(categoryId);
            var supplier = supplierService.findById(supplierId);

            product.setCategory(category);
            product.setSupplier(supplier);

            return productRepository.save(product);
        } catch (EntityNotFoundException e) {
            throw new BusinessRuleException(e.getMessage());
        }
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> findBySupplierId(Integer supplierId) {
        return productRepository.findByCategoryId(supplierId);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Product not found for id: " + id));
    }

    @Transactional
    public void delete(Integer id) {
        productRepository.delete(findById(id));
    }
}
