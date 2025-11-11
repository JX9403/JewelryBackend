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
public class ChatRequest {
    @NotBlank(message = "Question can not be empty or blank!")
    private String message;
    private String session_id;
}