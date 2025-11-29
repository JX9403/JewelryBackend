package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.entity.Category;
import com.ndn.JewelryBackend.entity.Like;
import com.ndn.JewelryBackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT l.product FROM Like l WHERE l.user.id = :userId")
    Page<Product> findLikedProducts(@Param("userId") Long userId, Pageable pageable);
    void deleteByProductId(Long id);

}

