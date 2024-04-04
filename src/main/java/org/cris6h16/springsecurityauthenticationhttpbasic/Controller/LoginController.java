package org.cris6h16.springsecurityauthenticationhttpbasic.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // MVC
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
