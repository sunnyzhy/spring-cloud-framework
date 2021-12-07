package org.springframework.security.oauth.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.admin.common.utils.ResponseUtil;
import org.springframework.cloud.admin.common.vo.ResponseEntityVo;
import org.springframework.security.oauth.bean.OAuthHttpBean;
import org.springframework.security.oauth.service.UserTokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@AllArgsConstructor
@Slf4j
public class AuthorizationController {
    private final OAuthHttpBean.AuthHttpConfig authHttpConfig;
    private final UserTokenService userTokenService;

    @GetMapping(value = "/oauth/login")
    public String loginPage(Model model) {
        model.addAttribute("loginUrl", authHttpConfig.getLoginProcessingUrl());
        return "login";
    }

    @PostMapping(value = "/oauth/user/token")
    @ResponseBody
    public ResponseEntityVo<String> token(@RequestBody Map<String, Object> payloadMap) {
        String token = null;
        try {
            token = userTokenService.createToken(payloadMap);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return ResponseUtil.ok(token);
    }

}
