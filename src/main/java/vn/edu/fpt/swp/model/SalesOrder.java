package vn.edu.fpt.swp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SalesOrder entity representing a sales order in the system
 */
public class SalesOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String orderNo;
    private Long customerId;
    private String status; // Draft / Confirmed / FulfillmentRequested / Completed / Cancelled
    private Long createdBy;
    private LocalDateTime createdAt;
    
    // Default constructor
    public SalesOrder() {
        this.status = "Draft";
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with parameters
    public SalesOrder(String orderNo, Long customerId, Long createdBy) {
        this();
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "SalesOrder{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", customerId=" + customerId +
                ", status='" + status + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }
}
