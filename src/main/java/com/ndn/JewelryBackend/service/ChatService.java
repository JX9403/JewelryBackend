package com.ndn.JewelryBackend.service;

import com.ndn.JewelryBackend.dto.request.ChatRequest;
import com.ndn.JewelryBackend.dto.request.EndSessionRequest;
import com.ndn.JewelryBackend.dto.response.ChatResponse;

public interface ChatService {
    ChatResponse ask(ChatRequest chatRequest);
    void endSession(EndSessionRequest endSessionRequest);
}
