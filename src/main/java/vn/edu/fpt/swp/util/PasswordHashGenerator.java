package vn.edu.fpt.swp.util;

/**
 * Command-line utility to generate password hashes
 * Usage: Run this class with a password argument to generate its hash
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("==============================================");
            System.out.println("Password Hash Generator for Smart WMS");
            System.out.println("==============================================");
            System.out.println("Usage: java PasswordHashGenerator <password>");
            System.out.println("");
            System.out.println("Example:");
            System.out.println("  java PasswordHashGenerator password123");
            System.out.println("");
            System.out.println("This will output a hash that can be inserted into the database.");
            System.out.println("==============================================");
            
            // Generate sample hashes for documentation
            System.out.println("\nGenerating sample hashes for common test passwords:");
            System.out.println("--------------------------------------------");
            generateAndPrint("password123");
            generateAndPrint("admin123");
            generateAndPrint("manager123");
            generateAndPrint("staff123");
            generateAndPrint("sales123");
            
            return;
        }
        
        // Generate hash for provided password
        String password = args[0];
        generateAndPrint(password);
    }
    
    /**
     * Generate and print password hash
     * @param password Password to hash
     */
    private static void generateAndPrint(String password) {
        String hash = PasswordUtil.hashPassword(password);
        System.out.println("Password: " + password);
        System.out.println("Hash:     " + hash);
        System.out.println("");
        System.out.println("SQL INSERT example:");
        System.out.println("INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status, CreatedAt)");
        System.out.println("VALUES ('username', 'Full Name', 'email@example.com', '" + hash + "', 'Staff', 'Active', GETDATE());");
        System.out.println("--------------------------------------------");
    }
}
