package com.ndn.JewelryBackend.dto.response;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {
    private Boolean status;
    private int code;
    private String message;
    private Object data;
    private Date timestamp;
}
