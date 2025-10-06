package com.ndn.JewelryBackend.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionResponse {
    private Long id;
    private String name;
    private String description;
}
