package com.example.inventorygenius.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

@RestController
public class ClientController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public ClientController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/getClientInfo")
    @CrossOrigin(origins = "https://github.com")
    public Map<String, Object> getClientInfo(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> emptyMap = new HashMap<>();
        try{
            if (principal != null) {
                return principal.getAttributes(); // Return user info from OAuth2.
            }
        } catch(Exception e) {
            return emptyMap;
        }
        return emptyMap;
        
        // throw new RuntimeException("User not authenticated");
    }

    @GetMapping("/loginSuccess")
public ResponseEntity<Map<String, String>> loginSuccess(
        @AuthenticationPrincipal OAuth2User principal) {
    Map<String, String> response = new HashMap<>();
    if (principal != null) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                "github", 
                principal.getName());

        if (authorizedClient != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();

            response.put("accessToken", accessToken);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }
    }
    response.put("message", "User not authenticated");
    return ResponseEntity.status(401).body(response);
}




    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // Invalidate the current session.
        return ResponseEntity.ok("Logged out successfully"); // Return success message.
    }
}
