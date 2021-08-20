package com.land.registry.auth.config;

import com.land.registry.auth.util.security.JwtAuthenticationEntryPoint;
import com.land.registry.auth.util.security.JwtAuthenticationFilter;
import javax.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @formatter:off
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
// @formatter:on
/**
 * This class provides custom Spring Security configuration by overriding some of the more general WebSecurityConfigurerAdapter settings.
 * <p/>
 * It implements Spring Security’s WebSecurityConfigurer interface and contains almost all the security configurations required for the JWT implementation.
 * <p/>
 * A Note on Supported Annotations:
 * @EnableWebSecurity This is the primary spring security annotation that is used to enable web security in a project.
 * @EnableGlobalMethodSecurity This is used to enable method level security based on annotations.
 * You can use following three types of annotations for securing your methods
 * @Secured("ROLE_ADMIN") public User getAllUsers() {}
 * @Secured({"ROLE_USER", "ROLE_ADMIN"})
 * public User getUser(Long id) {}
 * @Secured("IS_AUTHENTICATED_ANONYMOUSLY") public boolean isUsernameAvailable() {}
 * <p/>
 * jsr250Enabled: It enables the @RolesAllowed annotation that can be used like this:
 * @RolesAllowed("ROLE_ADMIN") public Poll createPoll() {}
 * <p/>
 * prePostEnabled: It enables more complex expression based access control syntax with @PreAuthorize and @PostAuthorize annotations:
 * @PreAuthorize("isAnonymous()") public boolean isUsernameAvailable() {}
 * @PreAuthorize("hasRole('USER')") public Poll createPoll() {}
 */ public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Inject
  private UserDetailsService customUserDetailsService;

  @Inject
  private JwtAuthenticationEntryPoint unauthorizedHandler;

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Override
  public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {

    //http.csrf().disable().authorizeRequests().anyRequest().permitAll();
    //http.headers().frameOptions().disable();

    // @formatter:off
    http.cors()
        .and()
        .csrf()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(unauthorizedHandler)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
            "/**/*.html", "/**/*.css", "/**/*.js")
        .permitAll()
        .antMatchers("/swagger-resources/**", "/v2/**")
        .permitAll()
        .antMatchers("/api/auth/**")
        .permitAll()
        .antMatchers("/api/user/checkUsernameAvailability/**")
        .permitAll()
        .antMatchers("/api/user/checkEmailAvailability/**")
        .permitAll()
        .anyRequest()
        .authenticated();
    // @formatter:on

    // Add our custom JWT security filter
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

}
