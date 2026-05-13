// ProductRepository.java
// src/main/java/com/example/erp/product/repository/ProductRepository.java

package com.example.erp.product.repository;

import com.example.erp.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}