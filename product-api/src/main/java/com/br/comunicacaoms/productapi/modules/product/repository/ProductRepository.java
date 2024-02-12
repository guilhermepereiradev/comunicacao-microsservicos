package com.br.comunicacaoms.productapi.modules.product.repository;

import com.br.comunicacaoms.productapi.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryId(Integer id);

    List<Product> findBySupplierId(Integer id);

}
