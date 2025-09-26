package com.example.inventorygenius.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String CHATBOT_API_URL = "http://localhost:8000/chat"; // Python FastAPI

    public String askChatbot(String question, String userEmail) {
        try {
            // Build request body
            Map<String, String> body = new HashMap<>();
            body.put("question", question);
            body.put("user_email", userEmail);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Wrap request
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            // Call FastAPI
            ResponseEntity<Map> response = restTemplate.postForEntity(CHATBOT_API_URL, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (String) response.getBody().get("answer");
            } else {
                return "Error: Chatbot API returned non-200 status";
            }
        } catch (Exception e) {
            return "Error calling chatbot API: " + e.getMessage();
        }
    }
}
