package com.dbfurniture.api.controller;

import com.dbfurniture.api.dto.InquiryDTO;
import com.dbfurniture.api.dto.InquirySubmitRequest;
import com.dbfurniture.service.CaptchaService;
import com.dbfurniture.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class InquiryController {
    private final CaptchaService captchaService;
    private final InquiryService inquiryService;

    public InquiryController(CaptchaService captchaService, InquiryService inquiryService) {
        this.captchaService = captchaService;
        this.inquiryService = inquiryService;
    }

    @PostMapping("/inquiries")
    public ResponseEntity<Map<String, Object>> submit(@RequestBody InquirySubmitRequest req) {
        Map<String, Object> res = new HashMap<>();

        if (req == null || req.phone == null || req.phone.trim().isEmpty()) {
            res.put("success", false);
            res.put("message", "电话不能为空");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        if (req.captchaId == null || req.captchaId.trim().isEmpty()
                || req.captchaCode == null || req.captchaCode.trim().isEmpty()) {
            res.put("success", false);
            res.put("message", "验证码不能为空");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        CaptchaService.CaptchaValidateResult captchaResult = captchaService.validate(req.captchaId, req.captchaCode);
        if (!captchaResult.ok) {
            res.put("success", false);
            res.put("message", captchaResult.message);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        InquiryDTO dto = inquiryService.submit(req);
        res.put("success", true);
        res.put("message", "提交成功，等待联系");
        res.put("inquiry", dto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

