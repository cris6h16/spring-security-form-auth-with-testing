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
import org.springframework.http.ResponseEntity;

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
        @DisplayName("whoIAm(Authentication authentication), paths & response code")
        void testWhoIAm_Way1() {
            ResponseEntity<?> res1 = testRestTemplate.getForEntity("/api/auth-info-way1", String.class);
            assertNotNull(res1);
            assertEquals(401, res1.getStatusCode().value());
        }

        @Test
        @DisplayName("whoIAm(), paths & response code")
        void testWhoIAm_Way2() {
            ResponseEntity<?> res2 = testRestTemplate.getForEntity("/api/auth-info-way2", String.class);
            assertNotNull(res2);
            assertEquals(401, res2.getStatusCode().value());
        }
    }

    @Nested
    class Auth {

        @BeforeEach
        void setUp() {
            restTemplateBuilder = restTemplateBuilder.basicAuthentication("cri6h16", "cri6h16");
            testRestTemplate = new TestRestTemplate(restTemplateBuilder);
        }

        @Test
        void testWhoIAm_Way1() {
            ResponseEntity<?> res1 = testRestTemplate.getForEntity("/api/auth-info-way1", String.class);
            assertNotNull(res1);
            assertEquals(200, res1.getStatusCode().value());
        }

        @Test
        void testWhoIAm_Way2() {
            ResponseEntity<?> res2 = testRestTemplate.getForEntity("/api/auth-info-way2", String.class);
            assertNotNull(res2);
            assertEquals(200, res2.getStatusCode().value());
        }


    }


}