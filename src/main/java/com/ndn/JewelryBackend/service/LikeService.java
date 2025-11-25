package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {

    boolean toggleLike(Long userId, Long productId);

    Page<ProductResponse> getLikedProducts(Long userId, Pageable pageable);
}
