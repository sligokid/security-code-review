package com.land.registry.auth.util.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  @Value("${app.jwtSecret}")
  private String jwtSecret;

  @Value("${app.jwtExpirationInMs}")
  private int jwtExpirationInMs;

  public String generateToken(final Authentication authentication) {
    final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    final Date now = new Date();
    final Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    return Jwts.builder().setSubject(Long.toString(userPrincipal.getId())).setIssuedAt(new Date())
        .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
  }

  public Long getUserIdFromJWT(final String token) {
    final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(final String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (final SignatureException ex) {
      logger.error("Invalid JWT signature");
    } catch (final MalformedJwtException ex) {
      logger.error("Invalid JWT token");
    } catch (final ExpiredJwtException ex) {
      logger.error("Expired JWT token");
    } catch (final UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token");
    } catch (final IllegalArgumentException ex) {
      logger.error("JWT claims string is empty.");
    }
    return false;
  }

}
