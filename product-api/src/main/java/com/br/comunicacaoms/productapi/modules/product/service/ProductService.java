package com.br.comunicacaoms.productapi.modules.product.service;

import com.br.comunicacaoms.productapi.modules.product.dto.*;
import com.br.comunicacaoms.productapi.modules.sales.clients.SalesClient;
import com.br.comunicacaoms.productapi.modules.sales.dto.SalesConfirmationDTO;
import com.br.comunicacaoms.productapi.modules.sales.enums.SalesStatus;
import com.br.comunicacaoms.productapi.modules.product.model.Product;
import com.br.comunicacaoms.productapi.modules.category.service.CategoryService;
import com.br.comunicacaoms.productapi.modules.sales.rabbitmq.SalesConfirmationSender;
import com.br.comunicacaoms.productapi.modules.product.repository.ProductRepository;
import com.br.comunicacaoms.productapi.modules.supplier.service.SupplierService;
import com.br.comunicacaoms.productapi.config.exceptions.BusinessRuleException;
import com.br.comunicacaoms.productapi.config.exceptions.EntityNotFoundException;
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
    private final SalesClient salesClient;

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

    public ProductSalesResponse findProductSales(Integer id) {
        var product = findById(id);
        try {
            var salesId = salesClient.findSalesByProductId(product.getId())
                    .orElseThrow(() -> new BusinessRuleException("The sales was not found by this product"));

            return new ProductSalesResponse(product, salesId);
        } catch (Exception ex) {
            throw new BusinessRuleException("There was an error trying to get the product's sales.");
        }
    }

    public SuccessResponse checkProductStock(ProductCheckStockRequest request) {
        if (isEmpty(request) || isEmpty(request.products())) {
            throw new BusinessRuleException("The request data and products must be informed.");
        }

        request.products().forEach(this::validadeStock);
        return new SuccessResponse("The stock is ok!");
    }

    private void validadeStock(ProductQuantityDTO productQuantityDTO) {
        if (isEmpty(productQuantityDTO.productId()) || isEmpty(productQuantityDTO.quantity())) {
            throw new BusinessRuleException("Product Id and quantity must be informed.");
        }

        var product = findById(productQuantityDTO.productId());
        if (productQuantityDTO.quantity() > product.getQuantityAvailable()) {
            throw new BusinessRuleException(String.format("The product %s is out of stock", product.getName()));
        }
    }
}
