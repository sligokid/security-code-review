package com.land.registry.auth.controller;

import com.land.registry.auth.model.dto.LoginRequest;
import com.land.registry.auth.model.dto.SignUpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIT {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    private final HttpHeaders headers = new HttpHeaders();

    @Test(expected = ResourceAccessException.class)
    public void userNotRegistered_signin_throws() {
        final String username = "myusername-not-registered";
        final String password = "mypassword-not-registered";

        // signin
        final LoginRequest signinPayload = new LoginRequest();
        signinPayload.setUsernameOrEmail(username);
        signinPayload.setPassword(password);

        final HttpEntity<LoginRequest> signinEntity = new HttpEntity<>(signinPayload, headers);

        restTemplate.exchange(createURLWithPort("api/auth/signin"), HttpMethod.POST, signinEntity, String.class);
    }

    @Test
    public void newUser_signup_succeeds() {
        final String username = "myusername1";
        final String password = "mypassword1";
        final String name = "myname1";
        final String email = "myuser1@mydomain.com";

        final SignUpRequest signupPayload = getSignUpRequest(username, password, name, email);

        final HttpEntity<SignUpRequest> signupPayloadEntity = new HttpEntity<>(signupPayload, headers);

        final ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("api/auth/signup"), HttpMethod.POST, signupPayloadEntity, String.class);

        final String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(actual.contains("api/users/myusername"));
    }

    private String createURLWithPort(final String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void newUser_signupAndsignin_succeeds() {
        final String username = "myusername2";
        final String password = "mypassword2";
        final String name = "myname2";
        final String email = "myuser2@mydomain.com";

        //signup
        final SignUpRequest signupPayload = getSignUpRequest(username, password, name, email);
        final HttpEntity<SignUpRequest> signupPayloadEntity = new HttpEntity<>(signupPayload, headers);

        final ResponseEntity<String> response = restTemplate
                .exchange(createURLWithPort("api/auth/signup"), HttpMethod.POST, signupPayloadEntity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // signin
        final LoginRequest signinPayload = new LoginRequest();
        signinPayload.setUsernameOrEmail(username);
        signinPayload.setPassword(password);

        final HttpEntity<LoginRequest> signinEntity = new HttpEntity<>(signinPayload, headers);

        final ResponseEntity<String> response2 = restTemplate
                .exchange(createURLWithPort("api/auth/signin"), HttpMethod.POST, signinEntity, String.class);

        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    private SignUpRequest getSignUpRequest(final String username, final String password, final String name, final String email) {
        // signup
        final SignUpRequest signupPayload = new SignUpRequest();
        signupPayload.setName(name);
        signupPayload.setUsername(username);
        signupPayload.setEmail(email);
        signupPayload.setPassword(password);
        return signupPayload;
    }

}
