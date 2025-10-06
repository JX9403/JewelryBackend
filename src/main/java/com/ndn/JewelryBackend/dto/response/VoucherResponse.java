package com.ndn.JewelryBackend.dto.response;

import com.ndn.JewelryBackend.enums.VoucherStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VoucherResponse {
    private Long id;
    private String code;
    private String description;
    private int quantity;
    private VoucherStatus status;
    private LocalDateTime sentAt;
    private LocalDateTime expiredAt;
}
