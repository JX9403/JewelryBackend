package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ImageResponse uploadImage(MultipartFile file, Boolean isUsedForAI);

    String embeddingAllProductImg();
}
