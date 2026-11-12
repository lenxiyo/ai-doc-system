package com.lvent.gateway.utils;

import com.lvent.common.exception.UnAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static SecretKey GenKey() throws NoSuchAlgorithmException {
        String customSecret = "yu";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(customSecret.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(bytes);
    }

    public static String GenJWT(Integer id, String username) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", id);
            claims.put("username", username);
            return Jwts.builder()
                    // 算法自动推导使用什么加密
                    .signWith(GenKey())
                    .claims(claims)
                    .expiration(new Date(System.currentTimeMillis() + 12 * 3600 * 1000))
                    .compact();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(GenKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    public static Integer validateToken(String token){
        if (token == null){
            throw new UnAuthException("token为空，未登录");
        }

        try {
            Claims claims = parseToken(token);
            // get方法后面可以跟一个类型，自动转换为需要的类型，防止报错，目前只知道claims的get方法有这个
            return claims.get("id",Integer.class);
        } catch (JwtException | IllegalArgumentException e) {
            // 令牌签名无效、格式错误或已过期
            throw new UnAuthException("令牌无效");
        }
    }
}
