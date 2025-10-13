package com.ndn.JewelryBackend.dto.request;

import lombok.Data;

@Data
public class SendVoucherRequest {
    private Long userId;
    private Long voucherId;
}
