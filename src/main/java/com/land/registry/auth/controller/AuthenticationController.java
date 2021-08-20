package com.land.registry.auth.controller;

import com.land.registry.auth.model.dto.ApiResponse;
import com.land.registry.auth.model.dto.JwtAuthenticationResponse;
import com.land.registry.auth.model.dto.LoginRequest;
import com.land.registry.auth.model.dto.SignUpRequest;
import com.land.registry.auth.service.AuthService;
import java.net.URI;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

  private final AuthService authService;

  @Inject
  public AuthenticationController(final AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/auth/signup")
  public ResponseEntity registerUser(@Valid @RequestBody final SignUpRequest signUpRequest) {
    final URI location = authService.registerUser(signUpRequest);

    return ResponseEntity.created(location)
        .body(new ApiResponse(true, "User registered successfully"));
  }

  @PostMapping("/auth/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {
    final String jwt = authService.authenticateUser(loginRequest);
    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
  }

}
