package com.ndn.JewelryBackend.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndn.JewelryBackend.exception.InsufficientStockException;
import com.ndn.JewelryBackend.service.AIService;
import com.pgvector.PGvector;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static com.ndn.JewelryBackend.util.Helper.buildMultipartBody;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    @Value("${URL_DEV_AI}")
    private String urlDevAi;

    @Override
    public PGvector productAnalyze(MultipartFile file) throws InsufficientStockException {
        try {
            String url = urlDevAi + "/product_analyze";

            HttpClient client = HttpClient.newHttpClient();

            // Gửi file dạng multipart
            HttpRequest.BodyPublisher bodyPublisher = buildMultipartBody(file);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "multipart/form-data; boundary=---JavaBoundary")
                    .POST(bodyPublisher)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Parse JSON về vector
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());

                List<Float> vec = new ArrayList<>();
                for (JsonNode num : root.get("vector")) {
                    vec.add(num.floatValue());
                }

                float[] arr = new float[vec.size()];
                for (int i = 0; i < vec.size(); i++) {
                    arr[i] = vec.get(i);
                }
                return new PGvector(arr);

            } else {
                throw new InsufficientStockException("FastAPI error: " + response.body());
            }
        } catch (Exception e) {
            throw new InsufficientStockException("Error calling FastAPI: " + e);
        }
    }
}
