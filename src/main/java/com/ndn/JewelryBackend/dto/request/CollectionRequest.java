package com.ndn.JewelryBackend.dto.request;

import lombok.Data;

@Data
public class CollectionRequest {
    private String name;
    private String description;
}