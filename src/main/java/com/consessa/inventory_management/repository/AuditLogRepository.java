package com.consessa.inventory_management.repository;

import com.consessa.inventory_management.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByAssetIdOrderByTimestampDesc(Long assetId);
    List<AuditLog> findAllByOrderByTimestampDesc();
}