package com.tr.token;

/**
 * NanoShield TokenGeneratorService
 * Author: Mehmet Akif Yüksel
 * Date: 2025
 * License: MIT – Please credit the original author when using.
 */
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

public class TokenGeneratorService {

    private static final String SECRET_KEY = "serviceASecretKey123"; // Gerçek uygulamada dışarıdan alınacak
    private static final String FINGERPRINT = "serviceA-xyz";

    public static String generateNanoShieldToken() {
        try {
            long timestamp = Instant.now().getEpochSecond();
            String nonce = generateNonce();
            String data = FINGERPRINT + ":" + timestamp + ":" + nonce;
            String hmac = generateHmac(data, SECRET_KEY);

            return FINGERPRINT + ":" + timestamp + ":" + nonce + ":" + hmac;
        } catch (Exception e) {
            throw new RuntimeException("Error generating NanoShield token", e);
        }
    }

    private static String generateNonce() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(random.nextInt(99999999));
    }

    private static String generateHmac(String data, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
