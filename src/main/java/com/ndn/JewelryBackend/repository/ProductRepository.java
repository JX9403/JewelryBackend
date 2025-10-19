package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCollectionId(Long collectionId);

    List<Product> findByCategoryId(Long categoryId);

    @Query("""
            SELECT p FROM Product p
            WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:categoryId IS NULL OR p.category.id = :categoryId)
              AND (:collectionId IS NULL OR p.collection.id = :collectionId)
              AND (:priceFrom IS NULL OR p.price >= :priceFrom)
              AND (:priceTo IS NULL OR p.price <= :priceTo)
              AND (:gender IS NULL OR p.gender = :gender)
            """)
    Page<Product> findAll(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("collectionId") Long collectionId,
            @Param("priceTo") BigDecimal priceTo,
            @Param("priceFrom") BigDecimal priceFrom,
            @Param("gender") String gender,
            Pageable pageable
    );

}
