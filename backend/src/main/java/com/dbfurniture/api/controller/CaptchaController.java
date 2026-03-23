package com.dbfurniture.api.controller;

import com.dbfurniture.api.dto.CaptchaDTO;
import com.dbfurniture.service.CaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/captcha")
    public CaptchaDTO captcha() {
        return captchaService.newCaptcha();
    }
}

