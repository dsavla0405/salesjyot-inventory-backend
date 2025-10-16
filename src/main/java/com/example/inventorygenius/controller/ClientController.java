package com.example.inventorygenius.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.inventorygenius.entity.Client;
import com.example.inventorygenius.service.ClientService;
import com.example.inventorygenius.service.JwtService;

import io.jsonwebtoken.Claims;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ClientController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private JwtService jwtService;

    public ClientController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    // ---------------- Logout ----------------
    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        SecurityContextHolder.clearContext();

        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set true if using HTTPS
        cookie.setMaxAge(0); // expire immediately
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out");
    }

    // ---------------- Get OAuth2 Client Info ----------------
    @GetMapping("/getClientInfo")
    public Map<String, Object> getClientInfo(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> emptyMap = new HashMap<>();
        try {
            if (principal != null) {
                return principal.getAttributes();
            }
        } catch (Exception e) {
            return emptyMap;
        }
        return emptyMap;
    }

    // ---------------- Login Success ----------------
    @GetMapping("/loginSuccess")
    public ResponseEntity<Map<String, String>> loginSuccess(OAuth2AuthenticationToken authentication) {
        Map<String, String> response = new HashMap<>();
        if (authentication != null) {
            String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    clientRegistrationId, authentication.getName());

            if (authorizedClient != null) {
                String accessToken = authorizedClient.getAccessToken().getTokenValue();
                response.put("accessToken", accessToken);
                response.put("message", "Login successful");
                return ResponseEntity.ok(response);
            }
        }
        response.put("message", "User not authenticated");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // ---------------- Get Logged-in User Details ----------------
    @GetMapping("/loggedInUser/getDetails")
    public ResponseEntity<Client> getUserDetails(@RequestParam String email,
                                                 @RequestHeader("Authorization") String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");
        Claims claims;
        try {
            claims = jwtService.validate(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!claims.getSubject().equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Client user = clientService.getUserDetails(email);
        return ResponseEntity.ok(user);
    }

    // ---------------- Update User Details ----------------
    @PostMapping("/loggedInUser/updateDetails")
    public ResponseEntity<Client> updateUserDetails(@RequestBody Client client) {
        Client updatedClient = clientService.updateUserDetails(client);
        return new ResponseEntity<>(updatedClient, HttpStatus.ACCEPTED);
    }

    // ---------------- Exchange Token ----------------
    @PostMapping("/exchange-token")
    public ResponseEntity<?> exchangeToken(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        Claims claims;
        try {
            claims = jwtService.validate(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        return ResponseEntity.ok(Map.of(
                "email", claims.getSubject(),
                "name", claims.get("name")
        ));
    }
}
