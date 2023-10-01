package com.alibou.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    /*
        Do this in the properties file not "hard coded". (Educational purpose only).
        Used website: https://seanwasere.com/generate-random-hex/.
     */
    private static final String SECRET_KEY = "ec178b9af0b245c17c657d986a71940dcb3d20ce9d16cf6df312edd232d43ea0";

    public String extractUsername(String token) {
        /*
         * getSubject: Extracts the subject of the token. In this case the subject is the username.
         */
        return extractClaim(token, Claims::getSubject);
    }

    // Method with a function to make easier to extract a specific Claim.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Method if we don't want to send extra claims.
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername()) // Because username is the unique field.
            .setIssuedAt(new Date(System.currentTimeMillis())) // Used to help us know if the tokens expired or not.
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) // The algorithm and key used to generate the JWT.
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        /*
         * To verify if the username of the token is the same username of the input
         *  and if the token is not expired.
         */
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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
