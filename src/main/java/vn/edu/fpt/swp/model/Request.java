package vn.edu.fpt.swp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Request entity representing a warehouse request (Inbound/Outbound/Transfer/Internal)
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String type; // Inbound / Outbound / Transfer / Internal
    private String status; // Created / Approved / InProgress / Completed / Rejected
    private Long createdBy;
    private Long approvedBy;
    private Long salesOrderId;
    private LocalDateTime createdAt;
    
    // Default constructor
    public Request() {
        this.status = "Created";
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with parameters
    public Request(String type, Long createdBy) {
        this();
        this.type = type;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public Long getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public Long getSalesOrderId() {
        return salesOrderId;
    }
    
    public void setSalesOrderId(Long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", createdBy=" + createdBy +
                ", approvedBy=" + approvedBy +
                ", salesOrderId=" + salesOrderId +
                ", createdAt=" + createdAt +
                '}';
    }
}
