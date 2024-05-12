package com.vishnuthangaraj.LensCorpAI.Security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtBlacklistService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    /*
        FUNCTION NAME : blacklistToken
        DESCRIPTION   : This method is designed to incorporate the existing JWT token into the
                        blacklisted token map upon user logout, thereby preventing the token’s
                        reuse for multiple logout instances.
        PARAMETER     : String (token) {JwtToken}
    */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /*
        FUNCTION NAME : isTokenBlacklisted
        DESCRIPTION   : This method is employed to ascertain whether the supplied JWT token is blacklisted
                        or remains valid for subsequent user operations.
        PARAMETER     : String (token) {JwtToken}
    */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
