package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.entity.Image;
import com.ndn.JewelryBackend.entity.Product;
import com.ndn.JewelryBackend.repository.ImageRepository;
import com.ndn.JewelryBackend.repository.ProductRepository;
import com.ndn.JewelryBackend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @Override
    public List<ImageResponse> uploadImages(Long productId, List<MultipartFile> files) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<ImageResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();


                HttpResponse<String> res = Unirest.post(supabaseUrl + "/storage/v1/object/" + bucket + "/" + fileName)
                        .header("Authorization", "Bearer " + supabaseKey)
                        .header("Content-Type", file.getContentType())
                        .body(file.getBytes())
                        .asString();

                if (res.getStatus() == 200 || res.getStatus() == 201) {
                    String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;

                    Image img = Image.builder()
                            .url(publicUrl)
                            .product(product)
                            .build();
                    imageRepository.save(img);

                    responses.add(ImageResponse.builder()
                            .id(img.getId())
                            .url(img.getUrl())
                            .productId(productId)
                            .build());
                } else {
                    throw new RuntimeException("Upload failed: " + res.getBody());
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading file", e);
            }
        }

        return responses;
    }

    @Override
    public List<ImageResponse> getImagesByProduct(Long productId) {
        return imageRepository.findByProductId(productId)
                .stream()
                .map(img -> ImageResponse.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .productId(productId)
                        .build())
                .toList();
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
