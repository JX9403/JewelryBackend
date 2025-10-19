package com.ndn.JewelryBackend.dto.request;

import com.ndn.JewelryBackend.dto.response.ImageResponse;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String gender;
    private Long categoryId;
    private Long collectionId;
    private List<ImageResponse> images;
}