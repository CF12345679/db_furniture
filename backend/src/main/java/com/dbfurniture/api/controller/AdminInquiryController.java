package com.dbfurniture.api.controller;

import com.dbfurniture.api.dto.AdminInquiryStatusRequest;
import com.dbfurniture.api.dto.InquiryDTO;
import com.dbfurniture.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminInquiryController {

    private static final String ADMIN_TOKEN = "dev-admin-token";

    private final InquiryService inquiryService;

    public AdminInquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    private boolean authorized(String token) {
        return token != null && ADMIN_TOKEN.equals(token);
    }

    @GetMapping("/inquiries")
    public ResponseEntity<List<InquiryDTO>> listInquiries(
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(inquiryService.listAll(), HttpStatus.OK);
    }

    @PutMapping("/inquiries/{id}")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @PathVariable("id") String inquiryId,
            @RequestBody AdminInquiryStatusRequest req
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Map<String, Object> res = new HashMap<>();
        boolean ok = inquiryService.updateStatus(inquiryId, req == null ? null : req.status);

        if (!ok) {
            res.put("success", false);
            res.put("message", "更新失败（id 或 status 不合法）");
            res.put("updatedAt", Instant.now());
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        res.put("success", true);
        res.put("message", "更新成功");
        res.put("updatedAt", Instant.now());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

