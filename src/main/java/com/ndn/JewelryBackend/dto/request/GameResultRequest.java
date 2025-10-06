package com.ndn.JewelryBackend.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResultRequest {
    private Long gameId;
    private Long userId;
    private int score;
}
