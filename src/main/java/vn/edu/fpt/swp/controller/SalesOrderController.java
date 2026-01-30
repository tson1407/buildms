package vn.edu.fpt.swp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.swp.model.Customer;
import vn.edu.fpt.swp.model.Product;
import vn.edu.fpt.swp.model.SalesOrder;
import vn.edu.fpt.swp.model.SalesOrderItem;
import vn.edu.fpt.swp.service.ProductService;
import vn.edu.fpt.swp.service.SalesOrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Sales Order operations (UC-SO-001 to UC-SO-004)
 */
@WebServlet("/salesorder")
public class SalesOrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SalesOrderService salesOrderService;
    private ProductService productService;
    
    @Override
    public void init() {
        salesOrderService = new SalesOrderService();
        productService = new ProductService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listSalesOrders(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "view":
                viewSalesOrder(request, response);
                break;
            case "confirm":
                confirmSalesOrder(request, response);
                break;
            case "cancel":
                showCancelForm(request, response);
                break;
            default:
                listSalesOrders(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "create":
                handleCreate(request, response);
                break;
            case "cancel":
                handleCancel(request, response);
                break;
            default:
                listSalesOrders(request, response);
                break;
        }
    }
    
    /**
     * List all sales orders
     */
    private void listSalesOrders(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<SalesOrder> orders = salesOrderService.getAllSalesOrders();
        request.setAttribute("orders", orders);
        request.setAttribute("pageTitle", "Sales Orders");
        request.getRequestDispatcher("/views/salesorder/list.jsp").forward(request, response);
    }
    
    /**
     * Show create sales order form (UC-SO-001)
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get all active customers
        List<Customer> customers = salesOrderService.getAllActiveCustomers();
        if (customers.isEmpty()) {
            request.setAttribute("error", "No customers available. Please create a customer first.");
            listSalesOrders(request, response);
            return;
        }
        
        // Get all active products
        List<Product> products = productService.getAllProducts();
        
        request.setAttribute("customers", customers);
        request.setAttribute("products", products);
        request.setAttribute("pageTitle", "Create Sales Order");
        request.getRequestDispatcher("/views/salesorder/create.jsp").forward(request, response);
    }
    
    /**
     * Handle create sales order (UC-SO-001)
     */
    private void handleCreate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        // Get form parameters
        String customerIdStr = request.getParameter("customerId");
        String[] productIds = request.getParameterValues("productId[]");
        String[] quantities = request.getParameterValues("quantity[]");
        
        // Validate input
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Customer is required");
            showCreateForm(request, response);
            return;
        }
        
        if (productIds == null || productIds.length == 0) {
            request.setAttribute("error", "At least one item is required");
            showCreateForm(request, response);
            return;
        }
        
        Long customerId;
        try {
            customerId = Long.parseLong(customerIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid customer ID");
            showCreateForm(request, response);
            return;
        }
        
        // Build order items
        List<SalesOrderItem> items = new ArrayList<>();
        for (int i = 0; i < productIds.length; i++) {
            try {
                Long productId = Long.parseLong(productIds[i]);
                Integer quantity = Integer.parseInt(quantities[i]);
                
                if (quantity <= 0) {
                    request.setAttribute("error", "Quantity must be greater than 0");
                    showCreateForm(request, response);
                    return;
                }
                
                // Check for duplicate products
                for (SalesOrderItem existingItem : items) {
                    if (existingItem.getProductId().equals(productId)) {
                        request.setAttribute("error", "Duplicate products are not allowed");
                        showCreateForm(request, response);
                        return;
                    }
                }
                
                SalesOrderItem item = new SalesOrderItem();
                item.setProductId(productId);
                item.setQuantity(quantity);
                items.add(item);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid product or quantity");
                showCreateForm(request, response);
                return;
            }
        }
        
        // Create sales order
        SalesOrder order = salesOrderService.createSalesOrder(customerId, userId, items);
        
        if (order != null) {
            response.sendRedirect(request.getContextPath() + "/salesorder?action=view&id=" + order.getId() + "&success=created");
        } else {
            request.setAttribute("error", "Failed to create sales order. Please check if customer and products are valid.");
            showCreateForm(request, response);
        }
    }
    
    /**
     * View sales order details
     */
    private void viewSalesOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/salesorder");
            return;
        }
        
        try {
            Long orderId = Long.parseLong(idStr);
            SalesOrder order = salesOrderService.getSalesOrderById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "Sales order not found");
                listSalesOrders(request, response);
                return;
            }
            
            List<SalesOrderItem> items = salesOrderService.getSalesOrderItems(orderId);
            
            request.setAttribute("order", order);
            request.setAttribute("items", items);
            request.setAttribute("pageTitle", "Sales Order Details");
            request.getRequestDispatcher("/views/salesorder/view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/salesorder");
        }
    }
    
    /**
     * Confirm sales order (UC-SO-002)
     */
    private void confirmSalesOrder(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                Long orderId = Long.parseLong(idStr);
                boolean success = salesOrderService.confirmSalesOrder(orderId, userId);
                
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/salesorder?action=view&id=" + orderId + "&success=confirmed");
                } else {
                    response.sendRedirect(request.getContextPath() + "/salesorder?action=view&id=" + orderId + "&error=confirm_failed");
                }
                return;
            } catch (NumberFormatException e) {
                // Invalid ID
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/salesorder");
    }
    
    /**
     * Show cancel form
     */
    private void showCancelForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/salesorder");
            return;
        }
        
        try {
            Long orderId = Long.parseLong(idStr);
            SalesOrder order = salesOrderService.getSalesOrderById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "Sales order not found");
                listSalesOrders(request, response);
                return;
            }
            
            request.setAttribute("order", order);
            request.setAttribute("pageTitle", "Cancel Sales Order");
            request.getRequestDispatcher("/views/salesorder/cancel.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/salesorder");
        }
    }
    
    /**
     * Handle cancel sales order (UC-SO-004)
     */
    private void handleCancel(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }
        
        String idStr = request.getParameter("id");
        String reason = request.getParameter("reason");
        
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                Long orderId = Long.parseLong(idStr);
                
                if (reason == null || reason.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/salesorder?action=cancel&id=" + orderId + "&error=reason_required");
                    return;
                }
                
                boolean success = salesOrderService.cancelSalesOrder(orderId, userId, reason);
                
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/salesorder?action=view&id=" + orderId + "&success=cancelled");
                } else {
                    response.sendRedirect(request.getContextPath() + "/salesorder?action=view&id=" + orderId + "&error=cancel_failed");
                }
                return;
            } catch (NumberFormatException e) {
                // Invalid ID
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/salesorder");
    }
}
