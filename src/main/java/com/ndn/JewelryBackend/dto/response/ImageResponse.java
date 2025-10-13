package com.ndn.JewelryBackend.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponse {
    private Long id;
    private String url;
}