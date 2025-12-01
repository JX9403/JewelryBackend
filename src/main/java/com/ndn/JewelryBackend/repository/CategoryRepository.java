package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    @Query("""
        SELECT c FROM Category c
        WHERE (:name IS NULL OR :name = '' OR LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%'))
    """)
    Page<Category> findAll(@Param("name") String name, Pageable pageable);

}

