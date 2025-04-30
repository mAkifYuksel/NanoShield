package com.tr.validate;
/**
 * NanoShield TokenGeneratorService
 * Author: Mehmet Akif Yüksel
 * Date: 2025
 * License: MIT – Please credit the original author when using.
 */

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public class TokenValidatorService {

    private static final String EXPECTED_FINGERPRINT = "serviceA-xyz"; //Burasi application.propertites veya vaultdan alınacak
    private static final String SECRET_KEY = "serviceASecretKey123";////Burasi application.propertites veya vaultdan alınacak
    private static final int TIMEOUT_SECONDS = 30;

    public static boolean validateNanoShieldToken(String token) {
        try {
            String[] parts = token.split(":");
            if (parts.length != 4) {
                return false;
            }

            String fingerprint = parts[0];
            long timestamp = Long.parseLong(parts[1]);
            String nonce = parts[2];
            String hmac = parts[3];
            long now = Instant.now().getEpochSecond();
           
            System.out.println("Şu anki zaman: " + now);
            System.out.println("Token zaman damgası: " + timestamp);
            System.out.println("Zaman farkı (sn): " + (now - timestamp));
            
            // Zaman kontrolü
           
            if (Math.abs(now - timestamp) > TIMEOUT_SECONDS) {
                return false; // Token süresi geçmiş
            }

            // Fingerprint kontrolü
            if (!EXPECTED_FINGERPRINT.equals(fingerprint)) {
                return false; // Yanlış servis
            }

            // HMAC kontrolü
            String data = fingerprint + ":" + timestamp + ":" + nonce;
            String calculatedHmac = generateHmac(data, SECRET_KEY);

            return hmac.equals(calculatedHmac);
        } catch (Exception e) {
            return false;
        }
    }

    private static String generateHmac(String data, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
