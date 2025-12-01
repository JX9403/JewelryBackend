package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    boolean existsByName(String name);

    @Query("""
        SELECT c FROM Collection c
        WHERE (:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<Collection> findAll(@Param("name") String name, Pageable pageable);

}
