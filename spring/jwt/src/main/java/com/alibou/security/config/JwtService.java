package com.alibou.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

    /*
        Do this in the properties file not "hard coded". (Educational purpose only).
        Used website: https://seanwasere.com/generate-random-hex/.
     */
    private static final String SECRET_KEY = "ec178b9af0b245c17c657d986a71940dcb3d20ce9d16cf6df312edd232d43ea0";

    public String extractUsername(String jwt) {
        return null;
    }

    /*
        Claims are information that are part of the JWT token.
     */
    /*
     * SingKey:
     *  Is a secret that let us digitally sign a JWT.
     *  It is used to create the signature part of the JWT.
     *  This signature is needed to verify if the sender is who
     *  it claims to be and ensure that the message isn't change
     *  along the way.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
