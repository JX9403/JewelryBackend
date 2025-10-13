package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<List<ImageResponse>> uploadImages(
            @RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(imageService.uploadImages(files));
    }


}
