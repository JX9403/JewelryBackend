package com.ndn.JewelryBackend.dto.response;

import com.ndn.JewelryBackend.enums.UserVoucherStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherResponse {
    private Long id;
    private String voucherCode;
    private Long voucherId;
    private Long userId;
    private String description;
    private LocalDateTime sentAt;
    private LocalDateTime expiredAt;
    private LocalDateTime usedAt;
    private UserVoucherStatus status;
}
