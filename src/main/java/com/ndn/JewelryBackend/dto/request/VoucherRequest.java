package com.ndn.JewelryBackend.dto.request;

import lombok.Data;

@Data
public class VoucherRequest {
    private String code;
    private String description;
    private int quantity;
}
