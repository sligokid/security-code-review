package com.land.registry.auth.util.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * This class provides a 401 unauthorized error to clients that try to access a protected resource
 * without proper authentication. It implements Spring Security’s AuthenticationEntryPoint
 * interface.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

  @Override
  public void commence(final HttpServletRequest httpServletRequest,
      final HttpServletResponse httpServletResponse,
      final AuthenticationException e) throws IOException, ServletException {

    logger.error("Responding with unauthorized error. Message - {}", e.getMessage());

    httpServletResponse
        .sendError(HttpServletResponse.SC_UNAUTHORIZED,
            "Sorry, You're not authorized to access this resource.");
  }

}
