package com.consessa.inventory_management.service;

import com.consessa.inventory_management.model.AuditLog;
import java.util.List;

public interface AuditService {
    void log(String action, String details, Long assetId);
    List<AuditLog> getAllLogs();
    List<AuditLog> getLogsForAsset(Long assetId);
}