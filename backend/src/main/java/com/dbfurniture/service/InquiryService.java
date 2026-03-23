package com.dbfurniture.service;

import com.dbfurniture.api.dto.InquiryDTO;
import com.dbfurniture.api.dto.InquirySubmitRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class InquiryService {

    private final List<InquiryDTO> inquiries = new CopyOnWriteArrayList<>();

    public InquiryService() {
        // MVP 占位：让你能立即在管理端看到线索列表并验证“改状态”功能
        InquiryDTO demo = new InquiryDTO();
        demo.id = "demo-inquiry-1";
        demo.name = "占位用户";
        demo.phone = "13800000000";
        demo.region = "XX省XX市";
        demo.description = "占位：想了解红木餐桌/柜类。";
        demo.status = "NEW";
        demo.createdAt = Instant.now();
        demo.updatedAt = demo.createdAt;
        inquiries.add(demo);
    }

    public InquiryDTO submit(InquirySubmitRequest req) {
        InquiryDTO dto = new InquiryDTO();
        dto.id = UUID.randomUUID().toString();
        dto.name = req.name;
        dto.phone = req.phone;
        dto.region = req.region;
        dto.description = req.description;
        dto.status = "NEW";
        dto.createdAt = Instant.now();
        dto.updatedAt = dto.createdAt;
        inquiries.add(dto);
        return dto;
    }

    public List<InquiryDTO> listAll() {
        return inquiries;
    }

    /**
     * 更新线索状态（MVP 内存版）。
     * status 允许：NEW / CONTACTED / IGNORED
     */
    public boolean updateStatus(String inquiryId, String status) {
        if (inquiryId == null || inquiryId.trim().isEmpty()) {
            return false;
        }
        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        String normalized = status.trim().toUpperCase();
        boolean allowed = "NEW".equals(normalized) || "CONTACTED".equals(normalized) || "IGNORED".equals(normalized);
        if (!allowed) {
            return false;
        }

        for (InquiryDTO dto : inquiries) {
            if (inquiryId.equals(dto.id)) {
                dto.status = normalized;
                dto.updatedAt = Instant.now();
                return true;
            }
        }
        return false;
    }
}

