package com.example.inventorygenius.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventorygenius.security.SecurityConfig;
import com.example.inventorygenius.service.ClientService;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import com.example.inventorygenius.entity.Client;
import com.example.inventorygenius.service.ClientService;

@RestController
public class ClientController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public ClientController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }
	
		@Autowired
	SecurityConfig sc;
		
	    @Autowired
	    ClientService clientservice;
	
	@PostMapping("api/auth/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
	    // Invalidate session
		System.out.println("logout----------------------"+request);
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }

	    // Clear security context
	    SecurityContextHolder.clearContext();

	    // Remove JSESSIONID cookie
	    Cookie cookie = new Cookie("JSESSIONID", null);
	    cookie.setPath("/");
	    cookie.setHttpOnly(true);
	    cookie.setValue(null);
	    cookie.setSecure(false); // set to true if using HTTPS
	    cookie.setMaxAge(0); // immediately expire
	    response.addCookie(cookie);

	    return ResponseEntity.ok("Logged out");
	}
	
	@GetMapping("/debug/sessions")
	public ResponseEntity<?> debugSessions() {
	    List<Object> principals = sc.sessionRegistry().getAllPrincipals();
	    System.out.println("sessionssssss----------------------"+principals.size());
	    for (Object principal : principals) {
	        List<SessionInformation> sessions = sc.sessionRegistry().getAllSessions(principal, false);
	        System.out.println("User: " + principal + " has " + sessions.size() + " session(s)");
	        for (SessionInformation session : sessions) {
//	        	session.expireNow();
	            System.out.println("  Session ID: " + session.getSessionId() + ", Expired: " + session.isExpired());
	        }
	    }
	    return ResponseEntity.ok("Sessions printed to console:"+principals.toString());
	}

    @GetMapping("/getClientInfo")
//    @CrossOrigin(origins = "https://github.com")
    public Map<String, Object> getClientInfo(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> emptyMap = new HashMap<>();
        //System.out.println("getClientInfo-----"+principal.getAttributes());
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
    public ResponseEntity<Map<String, String>> loginSuccess(OAuth2AuthenticationToken authentication) {
        Map<String, String> response = new HashMap<>();
        //System.out.println("--------------hitting login success url-------------");
        if (authentication != null) {
            String clientRegistrationId = authentication.getAuthorizedClientRegistrationId();
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    clientRegistrationId,
                    authentication.getName());

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



    @GetMapping("loggedInUser/getDetails")
    public Client getUserDetails(@RequestParam String email){
		Client userdetails=clientservice.getUserDetails(email);
//    	return ResponseEntity.ok(userdetails);
		//System.out.println("herreeeeeeeeeee-----------------------------"+userdetails);
		return userdetails;
  	
    }
    
    @PostMapping("loggedInUser/updateDetails")
    public ResponseEntity<Client> updateUserDetails(@RequestBody Client client){
    //	System.out.println("client data ::"+client.toString());
    	Client updatedclient=clientservice.updateUserDetails(client);
    	return new ResponseEntity<>(updatedclient, HttpStatus.ACCEPTED);
//    	return updatedclient;
    }


//    @GetMapping("/logout")
//    public ResponseEntity<String> logout(HttpSession session) {
//        session.invalidate(); // Invalidate the current session.
//        return ResponseEntity.ok("Logged out successfully"); // Return success message.
//    }
}
