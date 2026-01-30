package vn.edu.fpt.swp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.fpt.swp.model.*;
import vn.edu.fpt.swp.service.InboundService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Inbound Request operations
 */
@WebServlet("/inbound")
public class InboundController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private InboundService inboundService;
    
    @Override
    public void init() {
        inboundService = new InboundService();
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
                listRequests(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "view":
                viewRequestDetails(request, response);
                break;
            case "approve":
                showApprovalPage(request, response);
                break;
            case "execute":
                showExecutionPage(request, response);
                break;
            default:
                listRequests(request, response);
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
                handleCreateRequest(request, response);
                break;
            case "approve":
                handleApprove(request, response);
                break;
            case "reject":
                handleReject(request, response);
                break;
            case "startExecution":
                handleStartExecution(request, response);
                break;
            case "completeExecution":
                handleCompleteExecution(request, response);
                break;
            default:
                listRequests(request, response);
                break;
        }
    }
    
    /**
     * UC-INB-001 Step 1: List all inbound requests
     */
    private void listRequests(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String statusFilter = request.getParameter("status");
        
        List<Request> requests;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            requests = inboundService.getInboundRequestsByStatus(statusFilter);
        } else {
            requests = inboundService.getAllInboundRequests();
        }
        
        request.setAttribute("requests", requests);
        request.setAttribute("statusFilter", statusFilter);
        request.getRequestDispatcher("/views/inbound/list.jsp").forward(request, response);
    }
    
    /**
     * UC-INB-001 Step 2-3: Show create form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get warehouses and products for dropdowns
        List<Warehouse> warehouses = inboundService.getAllWarehouses();
        List<Product> products = inboundService.getAllProducts();
        
        request.setAttribute("warehouses", warehouses);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/views/inbound/create.jsp").forward(request, response);
    }
    
    /**
     * UC-INB-001 Step 4-10: Handle create request submission
     */
    private void handleCreateRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            // Get form data
            Long warehouseId = Long.parseLong(request.getParameter("warehouseId"));
            String expectedDateStr = request.getParameter("expectedDate");
            String notes = request.getParameter("notes");
            
            LocalDateTime expectedDate = null;
            if (expectedDateStr != null && !expectedDateStr.isEmpty()) {
                expectedDate = LocalDate.parse(expectedDateStr).atStartOfDay();
            }
            
            // Get items from form
            String[] productIds = request.getParameterValues("productId[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] locationIds = request.getParameterValues("locationId[]");
            
            // Validate items exist (Alternative Flow A1)
            if (productIds == null || productIds.length == 0) {
                throw new IllegalArgumentException("At least one item is required");
            }
            
            List<RequestItem> items = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                RequestItem item = new RequestItem();
                item.setProductId(Long.parseLong(productIds[i]));
                item.setQuantity(Integer.parseInt(quantities[i]));
                
                if (locationIds != null && i < locationIds.length && 
                    !locationIds[i].isEmpty()) {
                    item.setLocationId(Long.parseLong(locationIds[i]));
                }
                
                items.add(item);
            }
            
            // Create request
            Long requestId = inboundService.createInboundRequest(
                warehouseId, expectedDate, notes, items, user.getId()
            );
            
            response.sendRedirect(request.getContextPath() + 
                "/inbound?action=view&id=" + requestId + "&success=created");
            
        } catch (IllegalArgumentException e) {
            // Alternative Flow A2: Validation failed
            request.setAttribute("error", e.getMessage());
            showCreateForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to create request: " + e.getMessage());
            showCreateForm(request, response);
        }
    }
    
    /**
     * View request details
     */
    private void viewRequestDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long requestId = Long.parseLong(request.getParameter("id"));
            
            Request req = inboundService.getRequestById(requestId);
            List<RequestItem> items = inboundService.getRequestItems(requestId);
            
            if (req == null) {
                request.setAttribute("error", "Request not found");
                listRequests(request, response);
                return;
            }
            
            request.setAttribute("request", req);
            request.setAttribute("items", items);
            request.getRequestDispatcher("/views/inbound/view.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load request");
            listRequests(request, response);
        }
    }
    
    /**
     * UC-INB-002 Step 1-3: Show approval page
     */
    private void showApprovalPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long requestId = Long.parseLong(request.getParameter("id"));
            
            Request req = inboundService.getRequestById(requestId);
            List<RequestItem> items = inboundService.getRequestItems(requestId);
            
            if (req == null) {
                request.setAttribute("error", "Request not found");
                listRequests(request, response);
                return;
            }
            
            request.setAttribute("request", req);
            request.setAttribute("items", items);
            request.getRequestDispatcher("/views/inbound/approve.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load request");
            listRequests(request, response);
        }
    }
    
    /**
     * UC-INB-002 Step 5-8: Handle approval
     */
    private void handleApprove(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            Long requestId = Long.parseLong(request.getParameter("requestId"));
            
            boolean success = inboundService.approveRequest(requestId, user.getId());
            
            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/inbound?action=view&id=" + requestId + "&success=approved");
            } else {
                request.setAttribute("error", "Failed to approve request");
                showApprovalPage(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showApprovalPage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to approve request");
            showApprovalPage(request, response);
        }
    }
    
    /**
     * UC-INB-002 Alternative Flow A1: Handle rejection
     */
    private void handleReject(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            Long requestId = Long.parseLong(request.getParameter("requestId"));
            String reason = request.getParameter("rejectionReason");
            
            boolean success = inboundService.rejectRequest(requestId, user.getId(), reason);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/inbound?action=view&id=" + requestId + "&success=rejected");
            } else {
                request.setAttribute("error", "Failed to reject request");
                showApprovalPage(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showApprovalPage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to reject request");
            showApprovalPage(request, response);
        }
    }
    
    /**
     * UC-INB-003 Step 1-4: Show execution page
     */
    private void showExecutionPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long requestId = Long.parseLong(request.getParameter("id"));
            
            Request req = inboundService.getRequestById(requestId);
            List<RequestItem> items = inboundService.getRequestItems(requestId);
            
            if (req == null) {
                request.setAttribute("error", "Request not found");
                listRequests(request, response);
                return;
            }
            
            // Get locations for warehouse
            List<Location> locations = inboundService.getLocationsByWarehouse(req.getDestinationWarehouseId());
            
            request.setAttribute("request", req);
            request.setAttribute("items", items);
            request.setAttribute("locations", locations);
            request.getRequestDispatcher("/views/inbound/execute.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load request");
            listRequests(request, response);
        }
    }
    
    /**
     * UC-INB-003 Step 4: Start execution
     */
    private void handleStartExecution(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            Long requestId = Long.parseLong(request.getParameter("requestId"));
            
            boolean success = inboundService.startExecution(requestId, user.getId());
            
            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/inbound?action=execute&id=" + requestId + "&success=started");
            } else {
                request.setAttribute("error", "Failed to start execution");
                showExecutionPage(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showExecutionPage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to start execution");
            showExecutionPage(request, response);
        }
    }
    
    /**
     * UC-INB-003 Step 5-10: Complete execution
     */
    private void handleCompleteExecution(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            Long requestId = Long.parseLong(request.getParameter("requestId"));
            
            // Get received quantities from form
            String[] productIds = request.getParameterValues("productId[]");
            String[] receivedQuantities = request.getParameterValues("receivedQuantity[]");
            String[] locationIds = request.getParameterValues("locationId[]");
            
            List<RequestItem> receivedItems = new ArrayList<>();
            for (int i = 0; i < productIds.length; i++) {
                RequestItem item = new RequestItem();
                item.setProductId(Long.parseLong(productIds[i]));
                item.setReceivedQuantity(Integer.parseInt(receivedQuantities[i]));
                
                if (locationIds != null && i < locationIds.length && 
                    !locationIds[i].isEmpty()) {
                    item.setLocationId(Long.parseLong(locationIds[i]));
                }
                
                receivedItems.add(item);
            }
            
            boolean success = inboundService.completeExecution(requestId, user.getId(), receivedItems);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + 
                    "/inbound?action=view&id=" + requestId + "&success=completed");
            } else {
                request.setAttribute("error", "Failed to complete execution");
                showExecutionPage(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showExecutionPage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to complete execution: " + e.getMessage());
            showExecutionPage(request, response);
        }
    }
}
