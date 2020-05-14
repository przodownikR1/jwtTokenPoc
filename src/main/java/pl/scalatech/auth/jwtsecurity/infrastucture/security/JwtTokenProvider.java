package pl.scalatech.auth.jwtsecurity.infrastucture.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static pl.scalatech.auth.jwtsecurity.infrastucture.security.SecurityConstants.HEADER_STRING;
import static pl.scalatech.auth.jwtsecurity.infrastucture.security.SecurityConstants.TOKEN_PREFIX;


@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final JwtSetting jwtSetting;


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(KEY)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        try {
            return Jwts.parserBuilder()
                       .setSigningKey(KEY)
                       .build()
                       .parseClaimsJws(token)
                       .getBody()
                       .getExpiration()
                       .before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public static String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token -
//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//2. Sign the JWT using the HS512 algorithm and secret key.
//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        String secretKey = Base64.getEncoder()
                                 .encodeToString(jwtSetting.getSecret()
                                                           .getBytes());
        return TOKEN_PREFIX + Jwts.builder()
                                  .setClaims(claims)
                                  .setSubject(subject)
                                  .setIssuedAt(new Date(System.currentTimeMillis()))
                                  .setExpiration(new Date(System.currentTimeMillis() + jwtSetting.getSecExpired() * 1000))
                                  .signWith(KEY)
                                  .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
}