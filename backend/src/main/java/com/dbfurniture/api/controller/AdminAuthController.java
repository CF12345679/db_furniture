package com.dbfurniture.api.controller;

import com.dbfurniture.api.dto.AdminLoginRequest;
import com.dbfurniture.api.dto.AdminLoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    // MVP 单用户：后续替换为真正的登录/权限系统
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String ADMIN_TOKEN = "dev-admin-token";

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest req) {
        if (req == null || req.username == null || req.password == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ADMIN_USERNAME.equals(req.username) && ADMIN_PASSWORD.equals(req.password)) {
            AdminLoginResponse res = new AdminLoginResponse();
            res.token = ADMIN_TOKEN;
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}

