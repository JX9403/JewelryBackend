package com.ndn.JewelryBackend.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String gender;
    private int views;
    private String categoryName;
    private String collectionName;
    private List<ImageResponse> images;
}