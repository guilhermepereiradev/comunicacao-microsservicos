package com.br.comunicacaoms.productapi.repositories;

import com.br.comunicacaoms.productapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Integer> {

    List<Category> findByDescriptionContainingIgnoreCase(String description);
}
