package com.dbfurniture.api.controller;

import com.dbfurniture.api.dto.AdminInquiryStatusRequest;
import com.dbfurniture.api.dto.InquiryDTO;
import com.dbfurniture.service.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final InquiryService inquiryService;

    public AdminInquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping("/inquiries")
    public List<InquiryDTO> listInquiries() {
        return inquiryService.listAll();
    }

    @PutMapping("/inquiries/{id}")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable("id") String inquiryId,
            @RequestBody AdminInquiryStatusRequest req
    ) {
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

