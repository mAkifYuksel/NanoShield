package com.tr.controller;
/**
 * NanoShield TokenGeneratorService
 * Author: Mehmet Akif Yüksel
 * Date: 2025
 * License: MIT – Please credit the original author when using.
 */

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tr.token.TokenGeneratorService;

@RestController
@RequestMapping("/send")
public class SendController {

    @GetMapping
    public ResponseEntity<String> send() {
        String token = TokenGeneratorService.generateNanoShieldToken();
        System.out.println("Generated token: " + token); // Token her seferinde yeni mi?

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NanoShield-Auth", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            "http://localhost:8081/receive",
            HttpMethod.GET,
            entity,
            String.class
        );

        return ResponseEntity.ok("ServiceB’den gelen cevap: " + response.getBody());
    }
}

