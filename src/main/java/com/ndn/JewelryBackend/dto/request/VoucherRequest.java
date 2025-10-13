package com.ndn.JewelryBackend.dto.request;

import com.ndn.JewelryBackend.enums.VoucherStatus;
import lombok.Data;

@Data
public class VoucherRequest {
    private String code;
    private String description;
    private int quantity;
    private VoucherStatus status;

}
