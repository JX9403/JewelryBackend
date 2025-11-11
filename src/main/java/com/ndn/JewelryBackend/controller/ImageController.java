package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.dto.response.ImageResponse;
import com.ndn.JewelryBackend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> uploadImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam("isUsedForAI") Boolean isUsedForAI
    ) {
        ApiResponse apiResponse = ApiResponse.builder()
                .code(200)
                .status(true)
                .message("Successfully!")
                .data(imageService.uploadImage(file, isUsedForAI))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }


}
