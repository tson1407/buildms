package vn.edu.fpt.swp.service;

import vn.edu.fpt.swp.dao.*;
import vn.edu.fpt.swp.model.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Inbound Request operations
 */
public class InboundService {
    
    private RequestDAO requestDAO;
    private RequestItemDAO requestItemDAO;
    private WarehouseDAO warehouseDAO;
    private LocationDAO locationDAO;
    private ProductDAO productDAO;
    private InventoryDAO inventoryDAO;
    
    public InboundService() {
        this.requestDAO = new RequestDAO();
        this.requestItemDAO = new RequestItemDAO();
        this.warehouseDAO = new WarehouseDAO();
        this.locationDAO = new LocationDAO();
        this.productDAO = new ProductDAO();
        this.inventoryDAO = new InventoryDAO();
    }
    
    /**
     * Create a new inbound request with items
     * UC-INB-001
     */
    public Long createInboundRequest(Long warehouseId, LocalDateTime expectedDate, 
                                     String notes, List<RequestItem> items, Long userId) {
        // Validate warehouse exists
        Warehouse warehouse = warehouseDAO.getWarehouseById(warehouseId);
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse not found");
        }
        
        // Validate items not empty (BR-INB-002)
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("At least one item is required");
        }
        
        // Validate each item
        for (RequestItem item : items) {
            // Validate product exists and is active
            Product product = productDAO.getProductById(item.getProductId());
            if (product == null || !product.isActive()) {
                throw new IllegalArgumentException("Product is invalid or inactive");
            }
            
            // Validate quantity is positive (BR-INB-003)
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
        }
        
        // Create request
        Request request = new Request();
        request.setType("Inbound");
        request.setStatus("Created"); // BR-INB-004
        request.setCreatedBy(userId);
        request.setDestinationWarehouseId(warehouseId);
        request.setExpectedDate(expectedDate);
        request.setNotes(notes);
        request.setCreatedAt(LocalDateTime.now());
        
        Long requestId = requestDAO.createRequest(request);
        
        if (requestId == null) {
            throw new RuntimeException("Failed to create request");
        }
        
        // Create request items
        for (RequestItem item : items) {
            item.setRequestId(requestId);
            
            // Check for duplicate products (BR-INB-004 implied)
            if (requestItemDAO.productExistsInRequest(requestId, item.getProductId())) {
                throw new IllegalArgumentException("Duplicate product in request");
            }
            
            if (!requestItemDAO.createRequestItem(item)) {
                throw new RuntimeException("Failed to create request item");
            }
        }
        
        return requestId;
    }
    
    /**
     * Get all inbound requests
     */
    public List<Request> getAllInboundRequests() {
        return requestDAO.getRequestsByType("Inbound");
    }
    
    /**
     * Get inbound requests by status
     */
    public List<Request> getInboundRequestsByStatus(String status) {
        return requestDAO.getRequestsByTypeAndStatus("Inbound", status);
    }
    
    /**
     * Get request by ID with items
     */
    public Request getRequestById(Long id) {
        return requestDAO.getRequestById(id);
    }
    
    /**
     * Get items for a request
     */
    public List<RequestItem> getRequestItems(Long requestId) {
        return requestItemDAO.getItemsByRequestId(requestId);
    }
    
    /**
     * Approve an inbound request
     * UC-INB-002
     */
    public boolean approveRequest(Long requestId, Long userId) {
        // Validate request exists and has status "Created"
        Request request = requestDAO.getRequestById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Request not found");
        }
        
        // BR-APR-002: Only "Created" requests can be approved
        if (!"Created".equals(request.getStatus())) {
            throw new IllegalArgumentException("Only requests with status 'Created' can be approved");
        }
        
        return requestDAO.updateRequestStatus(requestId, "Approved", userId);
    }
    
    /**
     * Reject an inbound request
     * UC-INB-002 Alternative Flow A1
     */
    public boolean rejectRequest(Long requestId, Long userId, String reason) {
        // Validate request exists and has status "Created"
        Request request = requestDAO.getRequestById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Request not found");
        }
        
        if (!"Created".equals(request.getStatus())) {
            throw new IllegalArgumentException("Only requests with status 'Created' can be rejected");
        }
        
        // BR-APR-003: Rejection requires a reason
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Rejection reason is required");
        }
        
        return requestDAO.rejectRequest(requestId, userId, reason);
    }
    
    /**
     * Start execution of an inbound request
     * UC-INB-003
     */
    public boolean startExecution(Long requestId, Long userId) {
        Request request = requestDAO.getRequestById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Request not found");
        }
        
        // BR-EXE-002: Only "Approved" requests can be executed
        if (!"Approved".equals(request.getStatus())) {
            throw new IllegalArgumentException("Only approved requests can be executed");
        }
        
        return requestDAO.updateRequestStatus(requestId, "InProgress", userId);
    }
    
    /**
     * Complete execution of an inbound request
     * UC-INB-003
     */
    public boolean completeExecution(Long requestId, Long userId, List<RequestItem> receivedItems) {
        Request request = requestDAO.getRequestById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Request not found");
        }
        
        if (!"InProgress".equals(request.getStatus())) {
            throw new IllegalArgumentException("Only in-progress requests can be completed");
        }
        
        // Validate received quantities
        for (RequestItem item : receivedItems) {
            if (item.getReceivedQuantity() == null || item.getReceivedQuantity() < 0) {
                throw new IllegalArgumentException("Received quantity must be >= 0");
            }
        }
        
        // Update inventory for each item (BR-EXE-003)
        for (RequestItem item : receivedItems) {
            if (item.getReceivedQuantity() > 0) {
                // Determine location (use specified location or default)
                Long locationId = item.getLocationId();
                if (locationId == null) {
                    // Get first available location in warehouse
                    List<Location> locations = locationDAO.getLocationsByWarehouse(request.getDestinationWarehouseId());
                    if (!locations.isEmpty()) {
                        locationId = locations.get(0).getId();
                    } else {
                        throw new IllegalArgumentException("No available location in warehouse");
                    }
                }
                
                // Update inventory
                boolean inventoryUpdated = inventoryDAO.updateInventory(
                    item.getProductId(), 
                    request.getDestinationWarehouseId(), 
                    locationId, 
                    item.getReceivedQuantity()
                );
                
                if (!inventoryUpdated) {
                    throw new RuntimeException("Failed to update inventory");
                }
                
                // Update received quantity in request item
                requestItemDAO.updateReceivedQuantity(requestId, item.getProductId(), item.getReceivedQuantity());
            }
        }
        
        // Update request status to Completed
        return requestDAO.updateRequestStatus(requestId, "Completed", userId);
    }
    
    /**
     * Get all warehouses for dropdown
     */
    public List<Warehouse> getAllWarehouses() {
        return warehouseDAO.getAllWarehouses();
    }
    
    /**
     * Get all active products for dropdown
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    /**
     * Get locations for a warehouse
     */
    public List<Location> getLocationsByWarehouse(Long warehouseId) {
        return locationDAO.getLocationsByWarehouse(warehouseId);
    }
}
