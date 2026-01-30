package vn.edu.fpt.swp.service;

import vn.edu.fpt.swp.dao.CustomerDAO;
import vn.edu.fpt.swp.dao.ProductDAO;
import vn.edu.fpt.swp.dao.SalesOrderDAO;
import vn.edu.fpt.swp.model.Customer;
import vn.edu.fpt.swp.model.Product;
import vn.edu.fpt.swp.model.SalesOrder;
import vn.edu.fpt.swp.model.SalesOrderItem;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for Sales Order operations
 * Implements business logic for UC-SO-001 to UC-SO-004
 */
public class SalesOrderService {
    private SalesOrderDAO salesOrderDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    
    public SalesOrderService() {
        this.salesOrderDAO = new SalesOrderDAO();
        this.customerDAO = new CustomerDAO();
        this.productDAO = new ProductDAO();
    }
    
    /**
     * Create a new sales order (UC-SO-001)
     * @param customerId Customer ID
     * @param createdBy User ID creating the order
     * @param items List of order items
     * @return Created SalesOrder or null if failed
     */
    public SalesOrder createSalesOrder(Long customerId, Long createdBy, List<SalesOrderItem> items) {
        // Validate customer exists and is active
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null || !"Active".equals(customer.getStatus())) {
            return null;
        }
        
        // Validate items
        if (items == null || items.isEmpty()) {
            return null;
        }
        
        // Validate each item
        for (SalesOrderItem item : items) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product == null || !product.isActive()) {
                return null;
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return null;
            }
        }
        
        // Create sales order
        String orderNo = salesOrderDAO.generateOrderNumber();
        SalesOrder order = new SalesOrder();
        order.setOrderNo(orderNo);
        order.setCustomerId(customerId);
        order.setCreatedBy(createdBy);
        order.setStatus("Draft");
        order.setCreatedAt(LocalDateTime.now());
        
        Long orderId = salesOrderDAO.createSalesOrder(order);
        if (orderId == null) {
            return null;
        }
        
        // Create order items
        for (SalesOrderItem item : items) {
            item.setSalesOrderId(orderId);
            if (!salesOrderDAO.createSalesOrderItem(item)) {
                // Rollback would be handled by transaction in production
                return null;
            }
        }
        
        return order;
    }
    
    /**
     * Confirm a sales order (UC-SO-002)
     * @param orderId Order ID
     * @param confirmedBy User ID confirming the order
     * @return true if successful, false otherwise
     */
    public boolean confirmSalesOrder(Long orderId, Long confirmedBy) {
        SalesOrder order = salesOrderDAO.getSalesOrderById(orderId);
        if (order == null) {
            return false;
        }
        
        // Only Draft orders can be confirmed
        if (!"Draft".equals(order.getStatus())) {
            return false;
        }
        
        return salesOrderDAO.confirmSalesOrder(orderId, confirmedBy);
    }
    
    /**
     * Cancel a sales order (UC-SO-004)
     * @param orderId Order ID
     * @param cancelledBy User ID cancelling the order
     * @param reason Cancellation reason
     * @return true if successful, false otherwise
     */
    public boolean cancelSalesOrder(Long orderId, Long cancelledBy, String reason) {
        SalesOrder order = salesOrderDAO.getSalesOrderById(orderId);
        if (order == null) {
            return false;
        }
        
        // Cannot cancel completed orders
        if ("Completed".equals(order.getStatus())) {
            return false;
        }
        
        return salesOrderDAO.cancelSalesOrder(orderId, cancelledBy, reason);
    }
    
    /**
     * Get sales order by ID
     * @param orderId Order ID
     * @return SalesOrder or null if not found
     */
    public SalesOrder getSalesOrderById(Long orderId) {
        return salesOrderDAO.getSalesOrderById(orderId);
    }
    
    /**
     * Get all sales orders
     * @return List of sales orders
     */
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderDAO.getAllSalesOrders();
    }
    
    /**
     * Get order items for a specific order
     * @param orderId Order ID
     * @return List of order items
     */
    public List<SalesOrderItem> getSalesOrderItems(Long orderId) {
        return salesOrderDAO.getSalesOrderItems(orderId);
    }
    
    /**
     * Get all active customers
     * @return List of active customers
     */
    public List<Customer> getAllActiveCustomers() {
        return customerDAO.getAllActiveCustomers();
    }
}
