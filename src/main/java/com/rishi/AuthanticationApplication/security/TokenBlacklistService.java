package com.rishi.AuthanticationApplication.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    /**
     * token -> expiresAtEpochSeconds
     */
    private final Map<String, Long> revokedTokens = new ConcurrentHashMap<>();

    public void revoke(String token, Instant expiresAt) {
        if (token == null || token.isBlank() || expiresAt == null) {
            return;
        }
        revokedTokens.put(token, expiresAt.getEpochSecond());
    }

    public boolean isRevoked(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        Long exp = revokedTokens.get(token);
        if (exp == null) {
            return false;
        }

        long now = Instant.now().getEpochSecond();
        if (exp <= now) {
            // cleanup expired entries
            revokedTokens.remove(token);
            return false;
        }

        return true;
    }
}

