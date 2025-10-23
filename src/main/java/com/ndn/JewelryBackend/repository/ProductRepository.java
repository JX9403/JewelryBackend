package com.ndn.JewelryBackend.repository;

import com.ndn.JewelryBackend.dto.response.ProductSimilarityProjection;
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

    @Query(
            value = """
            WITH ProductDistances AS (
                SELECT
                    p.id AS product_id,
                    MIN(i.image_embedding <=> CAST(:vec AS vector)) AS min_distance
                FROM
                    products p
                JOIN
                    images i ON p.id = i.product_id
                WHERE
                    i.is_used_for_ai = true
                GROUP BY
                    p.id
            )
            SELECT
                p.id AS id,
                pd.min_distance AS minDistance
            FROM products p
            JOIN ProductDistances pd ON p.id = pd.product_id
            WHERE p.category_id = :category_id
            ORDER BY
                pd.min_distance ASC
        """,
            countQuery = """
            SELECT COUNT(DISTINCT p.id)
            FROM products p
            JOIN images i ON p.id = i.product_id
            WHERE i.is_used_for_ai = true
        """,
            nativeQuery = true
    )
    Page<ProductSimilarityProjection> findSimilarProductIds(
            @Param("category_id") Long categoryID,
            @Param("vec") String vec,
            Pageable pageable
    );

    @Query(
            value = """
            SELECT DISTINCT p
            FROM Product p
            LEFT JOIN FETCH p.images
            WHERE p.id IN :ids
        """
    )
    List<Product> findProductsWithImagesByIds(@Param("ids") List<Long> ids);
}
