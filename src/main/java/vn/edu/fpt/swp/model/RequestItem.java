package vn.edu.fpt.swp.model;

import java.io.Serializable;

/**
 * RequestItem entity representing an item in a warehouse request
 */
public class RequestItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long requestId;
    private Long productId;
    private Integer quantity;
    
    // Default constructor
    public RequestItem() {
    }
    
    // Constructor with parameters
    public RequestItem(Long requestId, Long productId, Integer quantity) {
        this.requestId = requestId;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getRequestId() {
        return requestId;
    }
    
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "RequestItem{" +
                "requestId=" + requestId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
