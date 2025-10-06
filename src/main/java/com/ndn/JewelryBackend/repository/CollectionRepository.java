package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    boolean existsByName(String name);
}
