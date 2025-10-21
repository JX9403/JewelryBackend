package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Collection;
import com.ndn.JewelryBackend.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("""
            SELECT c FROM Game c
            WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<Game> findAll(@Param("name") String name, Pageable pageable);

}
