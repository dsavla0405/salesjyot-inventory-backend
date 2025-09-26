package com.example.inventorygenius.controller;


import com.example.inventorygenius.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public String askChatbot(@RequestParam String question,
                             @RequestParam("user_email") String userEmail) {
        return chatService.askChatbot(question, userEmail);
    }
}

