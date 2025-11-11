package com.ndn.JewelryBackend.controller;

import com.ndn.JewelryBackend.dto.request.ChatRequest;
import com.ndn.JewelryBackend.dto.request.EndSessionRequest;
import com.ndn.JewelryBackend.dto.response.ApiResponse;
import com.ndn.JewelryBackend.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/ask")
    ResponseEntity<ApiResponse> ask(@Valid @RequestBody ChatRequest chatRequest){
        ApiResponse apiResponse = ApiResponse.builder()
                .status(true)
                .code(200)
                .message("Successfully!")
                .data(chatService.ask(chatRequest))
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/end-session")
    ResponseEntity<ApiResponse> endSession(@Valid @RequestBody EndSessionRequest endSessionRequest){
        chatService.endSession(endSessionRequest);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(true)
                .code(200)
                .message("Successfully!")
                .data(null)
                .timestamp(new Date())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
