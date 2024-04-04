package org.cris6h16.springsecurityauthenticationhttpbasic.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api")
public class PrincipalController {

    //    @GetMapping("/")
//    @GetMapping("")
    @RequestMapping(value = {"", "/"}, produces = {"text/html"}, method = {GET})
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> apiHome() {
        return ResponseEntity.status(200).body("Hello from API Home");
    }

    @RequestMapping("/auth-info-way1")
    @PreAuthorize("isAuthenticated()")
    //see DI in Spring if you don't understand this
    public String whoIAm(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();
        return "Hello from Who I Am, <b>" + username + "</b> with roles <b>" + roles + "</b>";
    }

    @RequestMapping("/auth-info-way2")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'ETC')")
    public String whoIAm() {
        // see Spring Security's architecture if you don't understand this
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();
        return "Hello from Who I Am, <b>" + username + "</b> with roles <b>" + roles + "</b>";
    }

    // there are more ways.....
}
