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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.tr.token.TokenGeneratorService;

@RestController
@RequestMapping("/send")
public class SendRetyContoller {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<String> sendWithRetry() {
        int maxRetries = 1; // İsteğe göre artırabilirsin
        int attempt = 0;

        while (attempt <= maxRetries) {
            String token = TokenGeneratorService.generateNanoShieldToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NanoShield-Auth", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        "http://localhost:8081/receive",
                        HttpMethod.GET,
                        entity,
                        String.class
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    return ResponseEntity.ok("Başarılı: " + response.getBody());
                }

            } catch (HttpClientErrorException.Unauthorized e) {
                System.out.println("Token expired. Retry ediliyor... ➡️");
                attempt++;
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hata: " + e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Retry sonrası hâlâ başarısız.");
    }
}

