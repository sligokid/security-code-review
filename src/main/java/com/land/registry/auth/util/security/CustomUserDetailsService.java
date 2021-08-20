package com.land.registry.auth.util.security;

import com.land.registry.auth.repository.UserRepository;
import com.land.registry.auth.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides User detail loading service to Spring Security.
 * <p/>
 * It allows Spring Security to Authenticate a User or perform various role-based checks. Note that,
 * the loadUserByUsername() method returns a UserDetails object that Spring Security uses for
 * performing various authentication and role based validations. Also defined is a custom
 * UserPrincipal class that will implement UserDetails interface, and return the UserPrincipal
 * object from loadUserByUsername() method.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String usernameOrEmail)
      throws UsernameNotFoundException {

    // Let people login with either username or email
    final User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException(
            "User not found with username or email : " + usernameOrEmail));

    return UserPrincipal.create(user);
  }

  // This method is used by JWTAuthenticationFilter
  @Transactional
  public UserDetails loadUserById(final Long id) {

    final User user = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

    return UserPrincipal.create(user);
  }

}
