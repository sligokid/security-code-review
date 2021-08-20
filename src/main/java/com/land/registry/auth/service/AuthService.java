package com.land.registry.auth.service;

import static com.land.registry.auth.model.enums.RoleName.ROLE_USER;

import com.google.common.base.Preconditions;
import com.land.registry.auth.repository.RoleRepository;
import com.land.registry.auth.repository.UserRepository;
import com.land.registry.auth.exception.AppException;
import com.land.registry.auth.exception.ResourceNotFoundException;
import com.land.registry.auth.model.dto.LoginRequest;
import com.land.registry.auth.model.dto.SignUpRequest;
import com.land.registry.auth.model.entity.Role;
import com.land.registry.auth.model.entity.User;
import com.land.registry.auth.util.security.JwtTokenProvider;
import java.net.URI;
import java.util.Collections;
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
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @Inject
  public AuthService(final UserRepository userRepository, final RoleRepository roleRepository,
      final PasswordEncoder passwordEncoder,
      final AuthenticationManager authenticationManager, final JwtTokenProvider tokenProvider) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
  }

  public void setAuthenticationContext(final Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  public Boolean isUsernameAvailable(final String username) {
    return !userRepository.existsByUsername(username);
  }

  public Boolean isEmailAvailable(final String email) {
    return !userRepository.existsByEmail(email);
  }

  public User findUserOrThrow(final String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
  }

  public URI registerUser(final SignUpRequest signUpRequest) {
    verifyDto(signUpRequest);

    final User user = getUser(signUpRequest);
    setRole(user);
    userRepository.save(user);

    return buildURI(signUpRequest.getUsername());
  }

  private void verifyDto(final SignUpRequest signUpRequest) {
    Preconditions.checkArgument(signUpRequest.getUsername() != null, "Username can't be null");
    Preconditions.checkArgument(signUpRequest.getEmail() != null, "Email can't be null");
    Preconditions.checkArgument(signUpRequest.getName() != null, "Name can't be null");
    Preconditions.checkArgument(signUpRequest.getPassword() != null, "Password can't be null");

    Preconditions.checkState(!userRepository.existsByUsername(signUpRequest.getUsername()),
        "Username is already taken. Please try another");

    Preconditions.checkState(!userRepository.existsByEmail(signUpRequest.getEmail()),
        "An account is already associated with this email");
  }

  private void setRole(final User user) {
    final Role userRole = roleRepository.findByName(ROLE_USER)
        .orElseThrow(() -> new AppException("User Role not set."));

    user.setRoles(Collections.singleton(userRole));
  }

  private URI buildURI(final String username) {
    return ServletUriComponentsBuilder.fromCurrentContextPath() //
        .path("/api/users/{username}") //
        .buildAndExpand(username) //
        .toUri();
  }

  User getUser(@Valid @RequestBody final SignUpRequest signUpRequest) {
    final String encPw = passwordEncoder.encode(signUpRequest.getPassword());

    return new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
        encPw);
  }

  public String authenticateUser(final LoginRequest loginRequest) {
    Preconditions
        .checkArgument(loginRequest.getUsernameOrEmail() != null, "Must Provide Email or Username");

    Preconditions.checkArgument(loginRequest.getPassword() != null, "Must Provide a Password");

    LOGGER.info("Username or Email: " + loginRequest.getUsernameOrEmail());

    LOGGER.info("Username/Email Exists? " + userRepository
        .existsByUsernameOrEmail(loginRequest.getUsernameOrEmail(),
            loginRequest.getUsernameOrEmail()));

    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(),
            loginRequest.getPassword()));

    setAuthenticationContext(authentication);
    return tokenProvider.generateToken(authentication);
  }

}
