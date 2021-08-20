package com.land.registry.auth.controller;

import com.land.registry.auth.model.dto.ApiResponse;
import com.land.registry.auth.model.dto.JwtAuthenticationResponse;
import com.land.registry.auth.model.dto.LoginRequest;
import com.land.registry.auth.model.dto.SignUpRequest;
import com.land.registry.auth.model.dto.UserIdentityAvailability;
import com.land.registry.auth.model.dto.UserProfile;
import com.land.registry.auth.model.dto.UserSummary;
import com.land.registry.auth.model.entity.User;
import com.land.registry.auth.service.AuthService;
import com.land.registry.auth.util.security.CurrentUser;
import com.land.registry.auth.util.security.UserPrincipal;
import java.net.URI;
import javax.inject.Inject;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

  private final AuthService authService;

  @Inject
  public AuthenticationController(final AuthService authService) {
    this.authService = authService;
  }

  @GetMapping("/user/me")
  @PreAuthorize("hasRole('USER')")
  public UserSummary getCurrentUser(@CurrentUser final UserPrincipal currentUser) {
    return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
  }


  @GetMapping("/user/checkUsernameAvailability")
  public UserIdentityAvailability checkUsernameAvailability(
      @RequestParam(value = "username") final String username) {
    return new UserIdentityAvailability(authService.isUsernameAvailable(username));
  }

  @GetMapping("/user/checkEmailAvailability")
  public UserIdentityAvailability checkEmailAvailability(
      @RequestParam(value = "email") final String email) {
    return new UserIdentityAvailability(authService.isEmailAvailable(email));
  }

  @GetMapping("/users/{username}")
  public UserProfile getUserProfile(@PathVariable(value = "username") final String username) {
    final User user = authService.findUserOrThrow(username);
    return new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getEmail());
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
