package no.group.employeemanagement.utils;

import no.group.employeemanagement.dto.JwtAuthenticationResponseDto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenUtil {

    private TokenUtil(){
    }
    public static final String KEY_CLAIM_USER_LOGIN = "userLogin";
    public static final String PREFIX_TOKEN = "token";
    public static final String PREFIX_REFRESH_TOKEN = "refreshToken";
    public static final String PREFIX_REFRESH_TOKEN_TTL = "refreshToken_ttl";

    public static Map<String, Object> generate(String username, Long userId){

        JwtAuthenticationResponseDto jwtAuthenticationResponseDto = new JwtAuthenticationResponseDto();
        var data = new HashMap<String, Object>();
        data.put(KEY_CLAIM_USER_LOGIN, jwtAuthenticationResponseDto);
        var token = JWTUtils.generateToken(username, data, Collections.singleton(""));
        var refreshToken = UUID.randomUUID().toString() + userId;

        Date date = new Date();
        long mills = date.getTime();
        Long ttl = mills + JWTUtils.EXPIRATIONTIME + JWTUtils.REFRESH_EXPIRATIONTIME;

        Map<String, Object> response = new HashMap<>();
        response.put(PREFIX_TOKEN, token);
        response.put(PREFIX_REFRESH_TOKEN, refreshToken);
        response.put(PREFIX_REFRESH_TOKEN_TTL, ttl);

        return response;
    }
}
