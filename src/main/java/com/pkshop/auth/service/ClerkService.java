package com.pkshop.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkshop.common.exception.UnauthorizedException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Service
public class ClerkService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ClerkService(ObjectMapper objectMapper, @Value("${clerk.secret-key}") String secretKey) {
        this.objectMapper = objectMapper;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.clerk.com/v1")
                .defaultHeader("Authorization", "Bearer " + secretKey).build();
    }

    public String verifyTokenAndGetEmail(String clerkToken) {
        try {
            String[] chunks = clerkToken.split("\\.");
            if(chunks.length < 2 ) throw new UnauthorizedException("รูปแบบ token ไม่ถูกต้อง");

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            JsonNode tokenNode = objectMapper.readTree(payload);

            if(!tokenNode.has("sid")) throw new UnauthorizedException("ไม่มี session ID ใน Token");
            String sid = tokenNode.get("sid").asText();

            // 🚀 แก้ไขจุดนี้: ใส่เครื่องหมาย / หลัง /sessions/ ให้ถูกต้อง
            JsonNode sessionNode = restClient.get()
                    .uri("/sessions/" + sid)
                    .retrieve()
                    .body(JsonNode.class);

            if (sessionNode == null || !"active".equals(sessionNode.get("status").asText())) {
                throw new UnauthorizedException("Session ของ Clerk หมดอายุหรือไม่ถูกต้อง");
            }

            String userId = sessionNode.get("user_id").asText();
            JsonNode userNode = restClient.get()
                    .uri("/users/" + userId)
                    .retrieve()
                    .body(JsonNode.class);

            if (userNode == null) throw new UnauthorizedException("ไม่พบข้อมูล User ใน Clerk");

            JsonNode emails = userNode.get("email_addresses");
            if (emails != null && emails.isArray() && !emails.isEmpty()) {
                return emails.get(0).get("email_address").asText();
            }

            throw new UnauthorizedException("ไม่พบ Email ในบัญชี Clerk นี้");
        }
        catch (Exception ex) {
            throw new UnauthorizedException("การยืนยันตัวตนกับ clerk ล้มเหลว: " + ex.getMessage());
        }
    }
}