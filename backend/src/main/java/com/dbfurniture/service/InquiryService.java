package com.dbfurniture.service;

import com.dbfurniture.api.dto.InquiryDTO;
import com.dbfurniture.api.dto.InquirySubmitRequest;
import com.dbfurniture.entity.Inquiry;
import com.dbfurniture.repository.InquiryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    public InquiryService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
        
        // MVP 占位数据：如果数据库为空则初始化
        if (inquiryRepository.count() == 0) {
            Inquiry demo = new Inquiry();
            demo.setId("demo-inquiry-1");
            demo.setName("占位用户");
            demo.setPhone("13800000000");
            demo.setRegion("XX省XX市");
            demo.setDescription("占位：想了解红木餐桌/柜类。");
            demo.setStatus("NEW");
            inquiryRepository.save(demo);
        }
    }

    @Transactional
    public InquiryDTO submit(InquirySubmitRequest req) {
        Inquiry entity = new Inquiry();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(req.name);
        entity.setPhone(req.phone);
        entity.setRegion(req.region);
        entity.setDescription(req.description);
        entity.setStatus("NEW");
        
        return toDTO(inquiryRepository.save(entity));
    }

    public List<InquiryDTO> listAll() {
        return inquiryRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean updateStatus(String inquiryId, String status) {
        if (inquiryId == null || inquiryId.trim().isEmpty() || status == null || status.trim().isEmpty()) {
            return false;
        }

        String normalized = status.trim().toUpperCase();
        boolean allowed = "NEW".equals(normalized) || "CONTACTED".equals(normalized) || "IGNORED".equals(normalized);
        if (!allowed) {
            return false;
        }

        return inquiryRepository.findById(inquiryId).map(entity -> {
            entity.setStatus(normalized);
            inquiryRepository.save(entity);
            return true;
        }).orElse(false);
    }

    private InquiryDTO toDTO(Inquiry entity) {
        InquiryDTO dto = new InquiryDTO();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.phone = entity.getPhone();
        dto.region = entity.getRegion();
        dto.description = entity.getDescription();
        dto.status = entity.getStatus();
        dto.createdAt = entity.getCreatedAt();
        dto.updatedAt = entity.getUpdatedAt();
        return dto;
    }
}
