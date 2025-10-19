package com.ndn.JewelryBackend.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameResponse {
    private Long id;
    private String name;
}
