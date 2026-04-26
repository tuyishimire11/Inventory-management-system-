package com.consessa.inventory_management.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String action;       // e.g., "CREATE", "UPDATE", "DELETE", "ISSUE", "RETURN"
    private String details;
    private Long assetId;
    private String performedBy;  // username from SecurityContext

    // Constructors
    public AuditLog() {}

    public AuditLog(String action, String details, Long assetId, String performedBy) {
        this.timestamp = LocalDateTime.now();
        this.action = action;
        this.details = details;
        this.assetId = assetId;
        this.performedBy = performedBy;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
}