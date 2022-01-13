package org.springframework.security.oauth.client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthorizationController {
    private final OAuth2RestTemplate authorizationCodeRestTemplate;

    @GetMapping(value = "/authorize", params = "grant_type=authorization_code")
    public String authorization_code_grant(Model model) {
        String token = authorizationCodeRestTemplate.getAccessToken().getValue();
        return token;
    }

    @GetMapping("/authorized")
    public String authorized(Model model) {
        String token = authorizationCodeRestTemplate.getAccessToken().getValue();
        return token;
    }

}
