package com.br.comunicacaoms.productapi.services;

import com.br.comunicacaoms.productapi.dtos.ProductQuantityDTO;
import com.br.comunicacaoms.productapi.dtos.ProductStockDTO;
import com.br.comunicacaoms.productapi.dtos.SalesConfirmationDTO;
import com.br.comunicacaoms.productapi.dtos.SalesStatus;
import com.br.comunicacaoms.productapi.model.Product;
import com.br.comunicacaoms.productapi.rabbitmq.SalesConfirmationSender;
import com.br.comunicacaoms.productapi.repositories.ProductRepository;
import com.br.comunicacaoms.productapi.services.exceptions.BusinessRuleException;
import com.br.comunicacaoms.productapi.services.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final SalesConfirmationSender salesConfirmationSender;

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

    public void updateProductStock(ProductStockDTO product) {
        try {
            validateStockUpdateData(product);
            updateStock(product);
        } catch (Exception ex) {
            log.error("Error while trying to update stock for message with error {}", ex.getMessage(), ex);
            var rejectMessage = new SalesConfirmationDTO(product.salesId(), SalesStatus.REJECT);
            salesConfirmationSender.sendSalesConfirmationMessage(rejectMessage);
        }
    }
    private void validateStockUpdateData(ProductStockDTO product) {
        if (isEmpty(product) || isEmpty(product.salesId())) {
            throw new BusinessRuleException("The product data and sales Id must be informed");
        }

        if (isEmpty(product.products())) {
            throw new BusinessRuleException("The sales' must be informed");
        }

        product.products().forEach(salesProduct -> {
            if (isEmpty(salesProduct.quantity()) || isEmpty(salesProduct.productId())) {
                throw new BusinessRuleException("The product Id and the quantity must be informed");
            }
        });
    }

    @Transactional
    private void updateStock(ProductStockDTO product) {
        var productsForUpdate = new ArrayList<Product>();
        product.products()
                .forEach(salesProduct -> {
                    var existingProduct = findById(salesProduct.productId());
                    validateQuantityInStock(salesProduct, existingProduct);

                    existingProduct.updateStock(salesProduct.quantity());
                    productsForUpdate.add(existingProduct);
                });

        if(!productsForUpdate.isEmpty()) {
            productRepository.saveAll(productsForUpdate);
            var approvedMessage = new SalesConfirmationDTO(product.salesId(), SalesStatus.APPROVED);
            salesConfirmationSender.sendSalesConfirmationMessage(approvedMessage);
        }
    }

    private void validateQuantityInStock(ProductQuantityDTO salesProduct, Product existingProduct) {
        if (salesProduct.quantity() > existingProduct.getQuantityAvailable()) {
            throw new BusinessRuleException(
                    String.format("The product %s is out of stock", existingProduct.getName()));
        }
    }
}
