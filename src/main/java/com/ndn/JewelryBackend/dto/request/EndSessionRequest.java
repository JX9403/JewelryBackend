package com.ndn.JewelryBackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndSessionRequest {
    @NotBlank(message = "SessionId can not be empty or blank!")
    private String session_id;
}
