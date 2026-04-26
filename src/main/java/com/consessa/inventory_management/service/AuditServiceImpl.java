package com.consessa.inventory_management.service;

import com.consessa.inventory_management.model.AuditLog;
import com.consessa.inventory_management.repository.AuditLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    private String getCurrentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }

    @Override
    public void log(String action, String details, Long assetId) {
        AuditLog log = new AuditLog(action, details, assetId, getCurrentUsername());
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }

    @Override
    public List<AuditLog> getLogsForAsset(Long assetId) {
        return auditLogRepository.findByAssetIdOrderByTimestampDesc(assetId);
    }
}