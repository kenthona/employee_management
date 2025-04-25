package no.group.employeemanagement.utils;

import io.jsonwebtoken.Jwts;
import no.group.employeemanagement.exceptions.ForbiddenException;
import no.group.employeemanagement.exceptions.ProcessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Component
public class JWTUtils {
    private static final Logger log = LoggerFactory.getLogger(JWTUtils.class);
    @Value("${ist.properties.data.expire-token-ms:900000}")
    public static long getExpirationTime;
    @Value("${ist.properties.data.expire-refresh-token-ms:3600000}")
    public static long getRefreshExpirationtime;
    public static long EXPIRATIONTIME;
    public static long REFRESH_EXPIRATIONTIME;
    static final String SECRET = "P+9QqFqgpNr+EqDDY4FX8vwEOMQ1xrKjCs0liBa90WaQ1OoXB6EcedxumDL5O0XmiIsgkxjU8yZcJ6sdpVBFXg==";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String INVALID_TOKEN = "Invalid Token";

    public JWTUtils() {
    }

    @Value("${ist.properties.data.expire-token-ms:900000}")
    public void setExpirationTime(long getExpirationTime) {
        EXPIRATIONTIME = getExpirationTime;
    }

    @Value("${ist.properties.data.expire-refresh-token-ms:3600000}")
    public void setRefreshExpirationTime(long getRefreshExpirationtime) {
        REFRESH_EXPIRATIONTIME = getRefreshExpirationtime;
    }

    public static String generateToken(String username, HashMap<String, Object> claimData, Set<String> role) {
        claimData.put("role", role);
        long systemDate = System.currentTimeMillis();
        Date startDateToken = new Date();
        Date endDateToken = new Date(systemDate + EXPIRATIONTIME);
        log.info("system date : {}", systemDate);
        log.info("start date token : {}", startDateToken);
        log.info("end date token : {}", endDateToken);
        log.info("milis expired token : {}", EXPIRATIONTIME);
        claimData.put("user", username);
        String JWT = Jwts.builder().setSubject(username).setIssuedAt(startDateToken).setClaims(claimData).setExpiration(endDateToken).signWith(getSigningKey()).compact();

        try {
            JWT = AESCrypto.encrypt(JWT);
        } catch (ProcessException var9) {
            log.error("failed to encrypt token", var9);
        }

        return JWT;
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        try {
            String token = getToken(request);
            if (token == null) {
                return null;
            } else {
                token = AESCrypto.decrypt(token.trim());
                if (token.startsWith("tokenSoa")) {
                    return null;
                } else {
                    Claims claims = (Claims)Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
                    return claims != null && !claims.isEmpty() ? new UsernamePasswordAuthenticationToken(claims.get("user"), claims.get("role"), Collections.emptyList()) : null;
                }
            }
        } catch (MalformedJwtException var3) {
            log.error("Invalid JWT token: {}", var3.getMessage());
            throw new ForbiddenException(INVALID_TOKEN);
        } catch (ExpiredJwtException var4) {
            log.error("JWT token is expired: {}", var4.getMessage());
            throw new ForbiddenException("Token Expired");
        } catch (UnsupportedJwtException var5) {
            log.error("JWT token is unsupported: {}", var5.getMessage());
            throw new ForbiddenException("Unsupported Token");
        } catch (IllegalArgumentException var6) {
            log.error("JWT claims string is empty: {}", var6.getMessage());
            throw new ForbiddenException("Token claims is empty");
        }
    }

    public static String claimStringValue(HttpServletRequest request, String key) {
        return String.valueOf(claimValue(request, key));
    }

    public static String claimStringValue(String token, String key) {
        return String.valueOf(claimValue(token, key));
    }

    public static <T> T claimObjectValue(HttpServletRequest request, String key, Class<T> clazz) {
        Object claimData = claimValue(request, key);
        return JsonConverterUtil.fromObject(claimData, clazz);
    }

    public static <T> T claimObjectValue(String token, String key, Class<T> clazz) {
        Object claimData = claimValue(token, key);
        return JsonConverterUtil.fromObject(claimData, clazz);
    }

    public static Long claimLongValue(HttpServletRequest request, String key) {
        return Long.parseLong(String.valueOf(claimValue(request, key)));
    }

    public static Long claimLongValue(String token, String key) {
        return Long.parseLong(String.valueOf(claimValue(token, key)));
    }

    public static Object claimValue(HttpServletRequest request, String key) {
        String token = getToken(request);
        if (token == null) {
            throw new ForbiddenException();
        } else {
            return claimValue(token, key);
        }
    }

    public static Object claimValue(String token, String key) {
        try {
            if (token == null) {
                return null;
            } else {
                token = AESCrypto.decrypt(token.trim());
                Claims claims = (Claims)Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
                return claims != null ? claims.get(key) : null;
            }
        } catch (MalformedJwtException var3) {
            log.error("Invalid JWT token: {}", var3.getMessage());
            throw new ForbiddenException(INVALID_TOKEN);
        } catch (ExpiredJwtException var4) {
            log.error("JWT token is expired: {}", var4.getMessage());
            throw new ForbiddenException("Token Expired");
        } catch (UnsupportedJwtException var5) {
            log.error("JWT token is unsupported: {}", var5.getMessage());
            throw new ForbiddenException("Unsupported Token");
        } catch (IllegalArgumentException var6) {
            log.error("JWT claims string is empty: {}", var6.getMessage());
            throw new ForbiddenException("Token claims is empty");
        }
    }

    private static Key getSigningKey() {
        byte[] keyBytes = (byte[])Decoders.BASE64.decode("P+9QqFqgpNr+EqDDY4FX8vwEOMQ1xrKjCs0liBa90WaQ1OoXB6EcedxumDL5O0XmiIsgkxjU8yZcJ6sdpVBFXg==");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return token == null ? null : token.replace("Bearer", "");
    }
}

