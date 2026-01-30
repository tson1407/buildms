package vn.edu.fpt.swp.service;

import vn.edu.fpt.swp.dao.*;
import vn.edu.fpt.swp.model.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Request operations (Inbound, Outbound, Transfer, Internal)
 * Implements business logic for UC-INB, UC-OUT, UC-TRF, UC-MOV
 */
public class RequestService {
    private static final Long DEFAULT_LOCATION_ID = 1L; // Default location when not specified
    
    private RequestDAO requestDAO;
    private WarehouseDAO warehouseDAO;
    private ProductDAO productDAO;
    private InventoryDAO inventoryDAO;
    
    public RequestService() {
        this.requestDAO = new RequestDAO();
        this.warehouseDAO = new WarehouseDAO();
        this.productDAO = new ProductDAO();
        this.inventoryDAO = new InventoryDAO();
    }
    
    /**
     * Create an inbound request (UC-INB-001)
     * @param createdBy User ID creating the request
     * @param destinationWarehouseId Destination warehouse ID
     * @param expectedDate Expected delivery date (optional)
     * @param notes Notes (optional)
     * @param items List of request items
     * @return Created Request or null if failed
     */
    public Request createInboundRequest(Long createdBy, Long destinationWarehouseId, 
                                       LocalDateTime expectedDate, String notes, 
                                       List<RequestItem> items) {
        // Validate warehouse exists
        Warehouse warehouse = warehouseDAO.getWarehouseById(destinationWarehouseId);
        if (warehouse == null) {
            return null;
        }
        
        // Validate items
        if (items == null || items.isEmpty()) {
            return null;
        }
        
        // Validate each item
        for (RequestItem item : items) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product == null || !product.isActive()) {
                return null;
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return null;
            }
        }
        
        // Create request
        Request request = new Request();
        request.setType("Inbound");
        request.setStatus("Created");
        request.setCreatedBy(createdBy);
        request.setDestinationWarehouseId(destinationWarehouseId);
        request.setExpectedDate(expectedDate);
        request.setNotes(notes);
        request.setCreatedAt(LocalDateTime.now());
        
        Long requestId = requestDAO.createRequest(request);
        if (requestId == null) {
            return null;
        }
        
        // Create request items
        for (RequestItem item : items) {
            item.setRequestId(requestId);
            if (!requestDAO.createRequestItem(item)) {
                return null;
            }
        }
        
        return request;
    }
    
    /**
     * Approve a request (UC-INB-002, UC-OUT-001)
     * @param requestId Request ID
     * @param approvedBy User ID approving
     * @return true if successful, false otherwise
     */
    public boolean approveRequest(Long requestId, Long approvedBy) {
        Request request = requestDAO.getRequestById(requestId);
        if (request == null || !"Created".equals(request.getStatus())) {
            return false;
        }
        
        return requestDAO.approveRequest(requestId, approvedBy);
    }
    
    /**
     * Reject a request (UC-INB-002, UC-OUT-001)
     * @param requestId Request ID
     * @param rejectedBy User ID rejecting
     * @param reason Rejection reason
     * @return true if successful, false otherwise
     */
    public boolean rejectRequest(Long requestId, Long rejectedBy, String reason) {
        Request request = requestDAO.getRequestById(requestId);
        if (request == null || !"Created".equals(request.getStatus())) {
            return false;
        }
        
        if (reason == null || reason.trim().isEmpty()) {
            return false;
        }
        
        return requestDAO.rejectRequest(requestId, rejectedBy, reason);
    }
    
    /**
     * Start execution of a request (UC-INB-003, UC-OUT-002)
     * @param requestId Request ID
     * @return true if successful, false otherwise
     */
    public boolean startExecution(Long requestId) {
        Request request = requestDAO.getRequestById(requestId);
        if (request == null || !"Approved".equals(request.getStatus())) {
            return false;
        }
        
        return requestDAO.startExecution(requestId);
    }
    
    /**
     * Complete inbound request execution (UC-INB-003)
     * Updates inventory based on received quantities
     * @param requestId Request ID
     * @param completedBy User ID completing
     * @return true if successful, false otherwise
     */
    public boolean completeInboundRequest(Long requestId, Long completedBy) {
        Request request = requestDAO.getRequestById(requestId);
        if (request == null || !"InProgress".equals(request.getStatus()) || !"Inbound".equals(request.getType())) {
            return false;
        }
        
        List<RequestItem> items = requestDAO.getRequestItems(requestId);
        
        // Update inventory for each item
        for (RequestItem item : items) {
            if (item.getReceivedQuantity() != null && item.getReceivedQuantity() > 0) {
                // Use specified location or default location
                Long locationId = item.getLocationId() != null ? item.getLocationId() : DEFAULT_LOCATION_ID;
                
                boolean success = inventoryDAO.increaseInventory(
                    item.getProductId(),
                    request.getDestinationWarehouseId(),
                    locationId,
                    item.getReceivedQuantity()
                );
                
                if (!success) {
                    return false;
                }
            }
        }
        
        // Mark request as completed
        return requestDAO.completeRequest(requestId, completedBy);
    }
    
    /**
     * Complete outbound request execution (UC-OUT-002)
     * Decreases inventory based on picked quantities
     * @param requestId Request ID
     * @param completedBy User ID completing
     * @return true if successful, false otherwise
     */
    public boolean completeOutboundRequest(Long requestId, Long completedBy) {
        Request request = requestDAO.getRequestById(requestId);
        if (request == null || !"InProgress".equals(request.getStatus()) || !"Outbound".equals(request.getType())) {
            return false;
        }
        
        List<RequestItem> items = requestDAO.getRequestItems(requestId);
        
        // Decrease inventory for each item
        for (RequestItem item : items) {
            if (item.getPickedQuantity() != null && item.getPickedQuantity() > 0) {
                // Use specified location or default location
                Long locationId = item.getLocationId() != null ? item.getLocationId() : DEFAULT_LOCATION_ID;
                
                boolean success = inventoryDAO.decreaseInventory(
                    item.getProductId(),
                    request.getSourceWarehouseId() != null ? request.getSourceWarehouseId() : request.getDestinationWarehouseId(),
                    locationId,
                    item.getPickedQuantity()
                );
                
                if (!success) {
                    return false; // Insufficient inventory
                }
            }
        }
        
        // Mark request as completed
        return requestDAO.completeRequest(requestId, completedBy);
    }
    
    /**
     * Update received quantity for an item (UC-INB-003)
     * @param requestId Request ID
     * @param productId Product ID
     * @param receivedQuantity Received quantity
     * @return true if successful, false otherwise
     */
    public boolean updateReceivedQuantity(Long requestId, Long productId, Integer receivedQuantity) {
        if (receivedQuantity == null || receivedQuantity < 0) {
            return false;
        }
        
        return requestDAO.updateReceivedQuantity(requestId, productId, receivedQuantity);
    }
    
    /**
     * Update picked quantity for an item (UC-OUT-002)
     * @param requestId Request ID
     * @param productId Product ID
     * @param pickedQuantity Picked quantity
     * @return true if successful, false otherwise
     */
    public boolean updatePickedQuantity(Long requestId, Long productId, Integer pickedQuantity) {
        if (pickedQuantity == null || pickedQuantity < 0) {
            return false;
        }
        
        return requestDAO.updatePickedQuantity(requestId, productId, pickedQuantity);
    }
    
    /**
     * Get request by ID
     * @param requestId Request ID
     * @return Request or null if not found
     */
    public Request getRequestById(Long requestId) {
        return requestDAO.getRequestById(requestId);
    }
    
    /**
     * Get all requests by type
     * @param type Request type
     * @return List of requests
     */
    public List<Request> getRequestsByType(String type) {
        return requestDAO.getRequestsByType(type);
    }
    
    /**
     * Get all requests
     * @return List of all requests
     */
    public List<Request> getAllRequests() {
        return requestDAO.getAllRequests();
    }
    
    /**
     * Get request items
     * @param requestId Request ID
     * @return List of request items
     */
    public List<RequestItem> getRequestItems(Long requestId) {
        return requestDAO.getRequestItems(requestId);
    }
    
    /**
     * Get all warehouses
     * @return List of warehouses
     */
    public List<Warehouse> getAllWarehouses() {
        return warehouseDAO.getAllWarehouses();
    }
}
