package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCollectionId(Long collectionId);
}
