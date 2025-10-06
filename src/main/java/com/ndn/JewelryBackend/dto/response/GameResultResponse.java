package com.ndn.JewelryBackend.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResultResponse {
    private Long id;
    private Long gameId;
    private String gameName;
    private Long userId;
    private String userName;
    private int score;
    private boolean voucherSent;
}
