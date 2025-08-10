package com.example.banvemaybay.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Component
public class JWTUtil {

    private final Key SECRET_KEY = Keys.hmacShaKeyFor("6ce90e09a30b372bbaae83da5f825d3751978d7111fe88aea06452ca1dd5ec73".getBytes());

    // Tạo token
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)            // Lưu username (email) vào subject
                .claim("roles", roles)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // Sử dụng Key thay vì String// Lưu vai trò vào claim "roles"
                .compact();                     // Tạo token và trả về
    }

    public Claims decodeToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)    // Thiết lập khóa bí mật
                    .build()
                    .parseClaimsJws(token)       // Phân tích JWT
                    .getBody();                  // Lấy các claims
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    // Lấy thông tin email từ token
    public String extractEmail(String token) {
        return decodeToken(token).getSubject(); // Lấy email từ subject trong token
    }

    // Lấy thông tin roles từ token
    public Set<String> extractRoles(String token) {
        List<String> rolesList = decodeToken(token).get("roles", List.class); // Lấy vai trò từ claim "roles"
        return rolesList.stream().collect(Collectors.toSet()); // Chuyển List thành Set

    }
}
