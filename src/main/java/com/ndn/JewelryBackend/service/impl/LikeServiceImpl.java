package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.response.ProductResponse;
import com.ndn.JewelryBackend.entity.Like;
import com.ndn.JewelryBackend.entity.Product;
import com.ndn.JewelryBackend.repository.LikeRepository;
import com.ndn.JewelryBackend.repository.ProductRepository;
import com.ndn.JewelryBackend.repository.UserRepository;
import com.ndn.JewelryBackend.service.LikeService;
import com.ndn.JewelryBackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    @Override
    @Transactional
    public boolean toggleLike(Long userId, Long productId) {
        boolean exists = likeRepository.existsByUserIdAndProductId(userId, productId);

        if (exists) {
            likeRepository.deleteByUserIdAndProductId(userId, productId);
            return false;
        }

        // Chưa like → tạo like mới
        Like like = new Like();
        like.setUser(userRepository.getReferenceById(userId));
        like.setProduct(productRepository.getReferenceById(productId));
        likeRepository.save(like);

        return true; // now liked
    }

    @Override
    public Page<ProductResponse> getLikedProducts(Long userId, Pageable pageable) {
        Page<Product> likePage = likeRepository.findLikedProducts(userId, pageable);
        return likePage.map(productService::toResponse);
    }
}
