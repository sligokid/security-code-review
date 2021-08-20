package com.land.registry.auth.service;

import com.land.registry.auth.model.dto.LoginRequest;
import com.land.registry.auth.model.dto.SignUpRequest;
import com.land.registry.auth.model.entity.User;
import com.land.registry.auth.repository.RoleRepository;
import com.land.registry.auth.repository.UserRepository;
import com.land.registry.auth.util.security.JwtTokenProvider;
import java.net.URI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Named
public class AuthService {

  private static final Logger LOGGER = Logger.getLogger(AuthService.class);

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @Inject
  public AuthService(final UserRepository userRepository, final RoleRepository roleRepository,
      final PasswordEncoder passwordEncoder,
      final AuthenticationManager authenticationManager, final JwtTokenProvider tokenProvider) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
  }

  public void setAuthenticationContext(final Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public URI registerUser(final SignUpRequest signUpRequest) {
    final User user = getUser(signUpRequest);
    userRepository.save(user);

    return buildURI(signUpRequest.getUsername());
  }

  private URI buildURI(final String username) {
    return ServletUriComponentsBuilder.fromCurrentContextPath() //
        .path("/api/users/{username}") //
        .buildAndExpand(username) //
        .toUri();
  }

  User getUser(@Valid @RequestBody final SignUpRequest signUpRequest) {
    return new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
        signUpRequest.getPassword());
  }

  public String authenticateUser(final LoginRequest loginRequest) {
    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),
            loginRequest.getPassword()));

    setAuthenticationContext(authentication);
    return tokenProvider.generateToken(authentication);
  }

}
