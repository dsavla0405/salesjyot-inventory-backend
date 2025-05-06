package com.example.inventorygenius.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HelloController {
    @GetMapping("/hello")
    public String getMethodName(){
        return "Hello !!";
    }
    
}
