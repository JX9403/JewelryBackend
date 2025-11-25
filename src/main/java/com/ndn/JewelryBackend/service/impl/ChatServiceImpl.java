package com.ndn.JewelryBackend.service.impl;

import com.ndn.JewelryBackend.dto.request.ChatRequest;
import com.ndn.JewelryBackend.dto.request.EndSessionRequest;
import com.ndn.JewelryBackend.dto.response.ChatResponse;
import com.ndn.JewelryBackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    @Value("${python.ai-url}")
    private String pythonAiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ChatResponse ask(ChatRequest chatRequest) {
        String url = pythonAiUrl + "chat";
        chatRequest.setSession_id(chatRequest.getSession_id());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);

        try {
            ResponseEntity<ChatResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ChatResponse.class
            );

            //Nếu trả về thành công thì response.getBody() chính là object bạn cần
            return response.getBody();

        } catch (Exception e) {
            throw new RuntimeException("Chatbot server error: " + e.getMessage(), e);
        }
    }

    @Override
    public void endSession(EndSessionRequest endSessionRequest) {
        RestTemplate restTemplate = new RestTemplate();

        String url = pythonAiUrl + "end_chat";

        try {
            restTemplate.postForObject(url, endSessionRequest, Void.class);
            System.out.println("Session " + endSessionRequest.getSession_id() + " stopped successfully.");
        } catch (Exception e) {
            System.err.println("Error stopping session: " + e.getMessage());
        }
    }
}
