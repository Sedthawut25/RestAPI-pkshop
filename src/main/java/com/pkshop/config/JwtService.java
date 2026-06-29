package com.pkshop.config;

import com.pkshop.domain.user.entity.Role;
import com.pkshop.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final AppProperties props;

    public JwtService(AppProperties props) {
        this.props = props;
    }

    private SecretKey key() {
        // ต้องยาวพอ (แนะนำ 64+ chars)
        return Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();

        return Jwts.builder()
                .issuer(props.issuer())
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(props.accessTokenMinutes(), ChronoUnit.MINUTES)))
                .signWith(key(), Jwts.SIG.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token);
    }
}
