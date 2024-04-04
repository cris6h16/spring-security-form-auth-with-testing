package org.cris6h16.springsecurityauthenticationhttpbasic.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/*
   Take in mind, this test is completely an EXAMPLE
   - avoid hard-coding
        - use @Value to get credentials from *.properties
        - etc.
   - try to Mock for complete the code coverage
   - try to declare endpoints in static final variables
   - test if your app is protected by some common vulnerabilities
   - etc.

   finally, again... this class is only an EXAMPLE
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrincipalControllerTest {

    private TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    Integer port;

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }


    @Nested
    @DisplayName("test methods without auth")
    class NoAuth {
        @Test
        @DisplayName("apiHome(), paths & response code")
        void apiHome() {
            ResponseEntity<?> res1 = testRestTemplate.getForEntity("/api", String.class);
            ResponseEntity<?> res2 = testRestTemplate.getForEntity("/api/", String.class);
            assertNotNull(res1);
            assertNotNull(res2);
            assertEquals(200, res1.getStatusCode().value());
            assertEquals(200, res2.getStatusCode().value());
        }

        @Test
        @DisplayName("whoIAm(Authentication authentication), paths & response body")
        void testWhoIAm_Way1() {
            ResponseEntity<?> res1 = testRestTemplate.getForEntity("/api/auth-info-way1", String.class);
            assertNotNull(res1);
            assertTrue(Objects.requireNonNull(res1.getBody()).toString().contains("<h1>Please Log In</h1>"));
        }

        @Test
        @DisplayName("whoIAm(), paths & response body")
        void testWhoIAm_Way2() {
            ResponseEntity<?> res2 = testRestTemplate.getForEntity("/api/auth-info-way2", String.class);
            assertNotNull(res2);
            assertTrue(Objects.requireNonNull(res2.getBody()).toString().contains("<h1>Please Log In</h1>"));
        }
    }

    @Nested
    class Auth {

        private String username;
        private String password;

        public record AuthRequest(String username, String password) {} // works like a DTO I recommend separate it in another file

        public Auth() {
            this.username = "cris6h16";
            this.password = "cris6h16";
        }

        @BeforeEach
        void setUpAuth() {
            // TODO: implement the auth process
        }


        @Test
        void testWhoIAm_Way1() {

            ResponseEntity<?> res1 = testRestTemplate.getForEntity("/api/auth-info-way1", String.class);
            assertNotNull(res1);
            assertTrue(Objects.requireNonNull(res1.getBody()).toString().contains("Hello from Who I Am, <b>" + username + "</b>"));

        }

        @Test
        void testWhoIAm_Way2() {
            ResponseEntity<?> res2 = testRestTemplate.getForEntity("/api/auth-info-way2", String.class);
            assertNotNull(res2);
            assertTrue(Objects.requireNonNull(res2.getBody()).toString().contains("Hello from Who I Am, <b>" + username + "</b>"));
        }


    }


}