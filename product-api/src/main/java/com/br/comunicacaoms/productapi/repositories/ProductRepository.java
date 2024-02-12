package com.br.comunicacaoms.productapi.repositories;

import com.br.comunicacaoms.productapi.model.Category;
import com.br.comunicacaoms.productapi.model.Product;
import com.br.comunicacaoms.productapi.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository <Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryId(Integer id);

    List<Product> findBySupplierId(Integer id);

}
