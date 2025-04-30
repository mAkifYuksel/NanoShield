package com.tr.controller;
/**
 * NanoShield TokenGeneratorService
 * Author: Mehmet Akif Yüksel
 * Date: 2025
 * License: MIT – Please credit the original author when using.
 */
import java.time.Instant;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tr.validate.TokenValidatorService;
@RestController
@RequestMapping("/receive")
public class ReceiveController {

    @GetMapping
    public ResponseEntity<String> receive(@RequestHeader("X-NanoShield-Auth") String token) {
        boolean valid = TokenValidatorService.validateNanoShieldToken(token);
      
        if (valid) {
            return ResponseEntity.ok("Geçerli Token. Erişim Onaylandı!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz veya Süresi Dolmuş Token!");
        }
    }
}

