package com.land.registry.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class enables CORS allowing access to the APIs from the react client that may run on its own
 * development server.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${rplra.web.cors.origins}")
  private String[] origins;

  /**
   * Adding Cross-Origin Resource Sharing (CORS) restrictions to limit access to API's from list of
   * provided origins (web addresses) and list of allowed methods to execute.
   *
   * @param registry {@link CorsRegistry}
   */
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins(origins)
        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE");
  }
}
