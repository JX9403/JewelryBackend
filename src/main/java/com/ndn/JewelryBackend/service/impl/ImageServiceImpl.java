package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.entity.Image;
import com.ndn.JewelryBackend.repository.ImageRepository;
import com.ndn.JewelryBackend.service.AIService;
import com.ndn.JewelryBackend.service.ImageService;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucket;

    private final ImageRepository imageRepository;
    private final AIService aiService;

    @Override
    public ImageResponse uploadImage(MultipartFile file, Boolean isUsedForAI) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            HttpResponse<String> res = Unirest.post(supabaseUrl + "/storage/v1/object/" + bucket + "/" + fileName)
                    .header("Authorization", "Bearer " + supabaseKey)
                    .header("Content-Type", file.getContentType())
                    .body(file.getBytes())
                    .asString();

            if (res.getStatus() == 200 || res.getStatus() == 201) {
                String publicUrl = supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;

                Image image = Image.builder()
                        .url(publicUrl)
                        .isUsedForAI(isUsedForAI)
                        .build();

                Image saved = imageRepository.save(image);

                return ImageResponse.builder()
                        .id(saved.getId())
                        .url(saved.getUrl())
                        .isUsedForAI(saved.getIsUsedForAI())
                        .build();
            } else {
                throw new RuntimeException("Upload failed: " + res.getBody());
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }

    @Override
    public String embeddingAllProductImg() {
        return aiService.AnalyzeAllImg();
    }


}
