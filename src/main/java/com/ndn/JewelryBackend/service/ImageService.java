package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<ImageResponse> uploadImages(Long productId, List<MultipartFile> files);
    List<ImageResponse> getImagesByProduct(Long productId);
    void deleteImage(Long id);
}