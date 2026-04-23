package com.backendmorphism.backend.ai.controller;

import com.backendmorphism.backend.ai.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    @Autowired
    private AiService aiService;

    @GetMapping("/test")
    public String testAi() {

        return aiService.generateResponse(
                "You are a Java interviewer",
                "Ask one Spring Boot interview question"
        );
    }
}