package com.land.registry.auth.service;

import com.land.registry.auth.repository.RoleRepository;
import com.land.registry.auth.repository.UserRepository;
import com.land.registry.auth.model.dto.LoginRequest;
import com.land.registry.auth.model.dto.SignUpRequest;
import com.land.registry.auth.model.entity.User;
import com.land.registry.auth.util.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private RoleRepository roleRepositoryMock;

    @Mock
    private JwtTokenProvider tokenProviderMock;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    private SignUpRequest signupRequest;

    private LoginRequest loginRequest;

    private AuthService authService;

    @Before
    public void before() {
        signupRequest = new SignUpRequest();
        loginRequest = new LoginRequest();
        authService = new AuthService(userRepositoryMock, roleRepositoryMock, passwordEncoderMock,
                authenticationManagerMock, tokenProviderMock);
    }

    @Test
    public void textPassword_getUser_encodesPassword() {
        final String pwText = "testPw";
        signupRequest.setPassword(pwText);

        final User response = authService.getUser(signupRequest);

        verify(passwordEncoderMock).encode(pwText);
    }

    @Test(expected = AuthenticationException.class)
    public void loginRequest_usermameNotFound_throws() {
        final String username = "userNotFound";
        final String pwText = "testPw";
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(pwText);
        when(authenticationManagerMock.authenticate(any())).thenThrow(new UsernameNotFoundException(""));

        authService.authenticateUser(loginRequest);
    }

    @Test
    public void loginRequest_usermameFound_authenticates() {
        final String username = "userNotFound";
        final String pwText = "testPw";
        loginRequest.setUsernameOrEmail(username);
        loginRequest.setPassword(pwText);
        when(authenticationManagerMock.authenticate(any())).thenReturn(getStubbedAuthentication());

        authService.authenticateUser(loginRequest);

        verify(tokenProviderMock).generateToken(any());
    }

    private Authentication getStubbedAuthentication() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(final boolean b) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

}
