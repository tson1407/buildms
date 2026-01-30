package vn.edu.fpt.swp.dao;

import vn.edu.fpt.swp.model.Customer;
import vn.edu.fpt.swp.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Customer entity
 */
public class CustomerDAO {
    
    /**
     * Get all active customers from the database
     * @return List of all active customers
     */
    public List<Customer> getAllActiveCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers WHERE Status = 'Active' ORDER BY Name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    /**
     * Get a customer by ID
     * @param id Customer ID
     * @return Customer object or null if not found
     */
    public Customer getCustomerById(Long id) {
        String sql = "SELECT * FROM Customers WHERE Id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCustomerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Extract Customer object from ResultSet
     * @param rs ResultSet
     * @return Customer object
     * @throws SQLException if database access error occurs
     */
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("Id"));
        customer.setCode(rs.getString("Code"));
        customer.setName(rs.getString("Name"));
        customer.setContactInfo(rs.getString("ContactInfo"));
        customer.setStatus(rs.getString("Status"));
        return customer;
    }
}
