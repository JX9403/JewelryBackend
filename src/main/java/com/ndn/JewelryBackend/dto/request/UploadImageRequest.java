package com.ndn.JewelryBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageRequest {
    private MultipartFile imgFile;
    private boolean isRaw = false;
}
