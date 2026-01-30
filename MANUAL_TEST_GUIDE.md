# Manual Test Execution Guide
## Smart WMS Authentication Features

**Purpose:** Step-by-step manual testing instructions for verifying Authentication & Authorization implementation  
**Target Audience:** QA Testers, Developers, Product Owners  
**Estimated Time:** 2-3 hours for complete test execution

---

## Table of Contents
1. [Test Environment Setup](#test-environment-setup)
2. [Test Data Preparation](#test-data-preparation)
3. [Test Execution](#test-execution)
4. [Test Result Recording](#test-result-recording)

---

## Test Environment Setup

### Prerequisites
- Java 17 installed
- Apache Maven 3.9+ installed
- SQL Server installed and running
- Apache Tomcat 10+ installed
- Web browser (Chrome, Firefox, or Edge)

### Setup Steps

#### 1. Build the Application
```bash
cd /home/runner/work/buildms/buildms
mvn clean package
```
**Expected:** Build succeeds with `target/buildms.war` created

#### 2. Initialize Database
```bash
# Connect to SQL Server
sqlcmd -S localhost -U your_username -P your_password

# Run schema creation
sqlcmd -S localhost -i database/schema.sql
```
**Expected:** Database `smartwms_db` created with all tables

#### 3. Create Test Users
Execute this SQL to create test accounts:

```sql
USE smartwms_db;
GO

-- Admin user
INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status) 
VALUES ('admin', 'Admin User', 'admin@test.com', 
        'salt:hash', 'Admin', 'Active');

-- Manager user
INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status) 
VALUES ('manager', 'Manager User', 'manager@test.com', 
        'salt:hash', 'Manager', 'Active');

-- Staff user
INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status) 
VALUES ('staff', 'Staff User', 'staff@test.com', 
        'salt:hash', 'Staff', 'Active');

-- Sales user
INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status) 
VALUES ('sales', 'Sales User', 'sales@test.com', 
        'salt:hash', 'Sales', 'Active');

-- Inactive user for testing
INSERT INTO Users (Username, Name, Email, PasswordHash, Role, Status) 
VALUES ('inactive', 'Inactive User', 'inactive@test.com', 
        'salt:hash', 'Staff', 'Inactive');
GO
```

**Note:** You need to generate proper password hashes. Use the application's registration feature or run this Java code:

```java
import vn.edu.fpt.swp.util.PasswordUtil;
public class HashGenerator {
    public static void main(String[] args) {
        System.out.println(PasswordUtil.hashPassword("password123"));
    }
}
```

#### 4. Deploy Application
```bash
# Copy WAR to Tomcat
cp target/buildms.war $TOMCAT_HOME/webapps/

# Start Tomcat
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
# OR
$TOMCAT_HOME/bin/startup.bat  # Windows
```

#### 5. Verify Deployment
- Open browser: `http://localhost:8080/buildms/`
- **Expected:** Login page loads successfully

---

## Test Data Preparation

### Test Credentials

| Username | Password | Role | Status | Purpose |
|----------|----------|------|--------|---------|
| admin | password123 | Admin | Active | Full access testing |
| manager | password123 | Manager | Active | Manager role testing |
| staff | password123 | Staff | Active | Staff role testing |
| sales | password123 | Sales | Active | Sales role testing |
| inactive | password123 | Staff | Inactive | Inactive account testing |

### Additional Test Data

Create these additional test users during testing:

| Username | Email | Purpose |
|----------|-------|---------|
| testuser1 | test1@test.com | Valid registration test |
| testuser2 | test2@test.com | Duplicate username test |
| testuser3 | test3@test.com | Change password test |

---

## Test Execution

### Test Suite 1: UC-AUTH-001 - User Login

#### Test Case 1.1: Valid Login - Admin Role
**Priority:** High  
**Prerequisites:** Admin user exists in database

**Steps:**
1. Navigate to `http://localhost:8080/buildms/auth?action=login`
2. Enter username: `admin`
3. Enter password: `password123`
4. Click "Sign In" button

**Expected Results:**
- ✅ Redirected to admin dashboard
- ✅ Session created (check browser dev tools → Application → Cookies)
- ✅ User welcome message displayed
- ✅ Navigation shows admin menu items

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.2: Valid Login - Manager Role
**Priority:** High  
**Prerequisites:** Manager user exists in database

**Steps:**
1. Navigate to login page
2. Enter username: `manager`
3. Enter password: `password123`
4. Click "Sign In"

**Expected Results:**
- ✅ Redirected to manager dashboard
- ✅ Session created
- ✅ Manager menu items visible

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.3: Valid Login - Staff Role
**Priority:** High

**Steps:**
1. Navigate to login page
2. Enter username: `staff`
3. Enter password: `password123`
4. Click "Sign In"

**Expected Results:**
- ✅ Redirected to staff dashboard
- ✅ Limited menu items (no admin/user management)

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.4: Valid Login - Sales Role
**Priority:** High

**Steps:**
1. Navigate to login page
2. Enter username: `sales`
3. Enter password: `password123`
4. Click "Sign In"

**Expected Results:**
- ✅ Redirected to sales dashboard
- ✅ Sales-related menu items visible

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.5: Empty Username
**Priority:** High

**Steps:**
1. Navigate to login page
2. Leave username field empty
3. Enter password: `test123`
4. Click "Sign In"

**Expected Results:**
- ✅ Error message: "Username and password are required"
- ✅ Remains on login page
- ✅ No session created

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.6: Empty Password
**Priority:** High

**Steps:**
1. Navigate to login page
2. Enter username: `admin`
3. Leave password field empty
4. Click "Sign In"

**Expected Results:**
- ✅ Error message: "Username and password are required"
- ✅ Remains on login page

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.7: Invalid Username
**Priority:** High

**Steps:**
1. Navigate to login page
2. Enter username: `nonexistent`
3. Enter password: `password123`
4. Click "Sign In"

**Expected Results:**
- ✅ Error message: "Invalid username or password"
- ✅ Remains on login page
- ✅ No specific hint about username not existing

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.8: Invalid Password
**Priority:** High

**Steps:**
1. Navigate to login page
2. Enter username: `admin`
3. Enter password: `wrongpassword`
4. Click "Sign In"

**Expected Results:**
- ✅ Error message: "Invalid username or password"
- ✅ Remains on login page

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 1.9: Inactive Account
**Priority:** Medium

**Steps:**
1. Navigate to login page
2. Enter username: `inactive`
3. Enter password: `password123`
4. Click "Sign In"

**Expected Results:**
- ✅ Error message displayed (either generic or specific about deactivation)
- ✅ Login rejected

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

**Note:** Detail design specifies "Your account has been deactivated. Please contact administrator." but generic message is also acceptable for security.

---

#### Test Case 1.10: LastLogin Timestamp Update
**Priority:** Medium

**Steps:**
1. Note current lastLogin value in database for admin user:
   ```sql
   SELECT LastLogin FROM Users WHERE Username = 'admin';
   ```
2. Login as admin
3. Check database again

**Expected Results:**
- ✅ LastLogin timestamp is updated to current time

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

### Test Suite 2: UC-AUTH-002 - User Registration (Admin Only)

#### Test Case 2.1: Valid User Registration
**Priority:** High  
**Prerequisites:** Logged in as admin

**Steps:**
1. Navigate to `/user`
2. Click "Create User" or navigate to `/user?action=create`
3. Fill form:
   - Username: `testuser1`
   - Name: `Test User One`
   - Email: `test1@test.com`
   - Password: `test123456`
   - Confirm Password: `test123456`
   - Role: `Staff`
   - Status: `Active`
4. Click "Create User"

**Expected Results:**
- ✅ Success message: "User testuser1 created successfully"
- ✅ Redirected to user list
- ✅ New user appears in list
- ✅ Check database: Password is hashed, not plain text

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 2.2: Duplicate Username
**Priority:** High

**Steps:**
1. Navigate to create user form
2. Enter username: `admin` (existing user)
3. Fill other fields with valid data
4. Submit form

**Expected Results:**
- ✅ Error message: "Username is already taken"
- ✅ Form shows validation error
- ✅ User not created

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 2.3: Duplicate Email
**Priority:** High

**Steps:**
1. Navigate to create user form
2. Enter email: `admin@test.com` (existing email)
3. Fill other fields with valid data
4. Submit form

**Expected Results:**
- ✅ Error message: "Email is already registered"
- ✅ User not created

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 2.4: Invalid Username (Too Short)
**Priority:** Medium

**Steps:**
1. Navigate to create user form
2. Enter username: `ab` (< 3 characters)
3. Fill other fields
4. Submit

**Expected Results:**
- ✅ Validation error: Username must be 3-50 characters
- ✅ User not created

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 2.5: Invalid Email Format
**Priority:** Medium

**Steps:**
1. Navigate to create user form
2. Enter email: `notanemail`
3. Fill other fields
4. Submit

**Expected Results:**
- ✅ Validation error: Invalid email format
- ✅ HTML5 validation may prevent submission

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 2.6: Password Too Short
**Priority:** High

**Steps:**
1. Navigate to create user form
2. Enter password: `12345` (< 6 characters)
3. Fill other fields
4. Submit

**Expected Results:**
- ✅ Validation error: Password must be at least 6 characters
- ✅ User not created

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 2.7: Password Mismatch
**Priority:** High

**Steps:**
1. Navigate to create user form
2. Enter password: `password123`
3. Enter confirm password: `password456`
4. Submit

**Expected Results:**
- ✅ Validation error: Passwords do not match
- ✅ User not created

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 2.8: Access Denied for Non-Admin
**Priority:** High

**Steps:**
1. Logout if logged in
2. Login as `staff`
3. Try to navigate to `/user`

**Expected Results:**
- ✅ Access denied / 403 error
- ✅ Message: "You don't have permission to access this resource"

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

### Test Suite 3: UC-AUTH-003 - Change Password

#### Test Case 3.1: Valid Password Change
**Priority:** High

**Steps:**
1. Login as any user (e.g., `testuser1`)
2. Navigate to `/auth?action=changePassword`
3. Enter current password: `test123456`
4. Enter new password: `newpass123`
5. Enter confirm password: `newpass123`
6. Submit

**Expected Results:**
- ✅ Success message: "Password changed successfully"
- ✅ Logout and login with new password succeeds
- ✅ Login with old password fails

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 3.2: Incorrect Current Password
**Priority:** High

**Steps:**
1. Navigate to change password page
2. Enter current password: `wrongpassword`
3. Enter new password: `newpass123`
4. Enter confirm password: `newpass123`
5. Submit

**Expected Results:**
- ✅ Error message: "Current password is incorrect"
- ✅ Password not changed

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 3.3: New Password Same as Current
**Priority:** Medium

**Steps:**
1. Navigate to change password page
2. Enter current password: `test123456`
3. Enter new password: `test123456` (same)
4. Enter confirm password: `test123456`
5. Submit

**Expected Results:**
- ✅ Error message: New password must be different
- ✅ Password not changed

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 3.4: New Password Too Short
**Priority:** High

**Steps:**
1. Navigate to change password page
2. Enter current password correctly
3. Enter new password: `12345` (< 6 chars)
4. Submit

**Expected Results:**
- ✅ Validation error
- ✅ Password not changed

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 3.5: Password Confirmation Mismatch
**Priority:** High

**Steps:**
1. Navigate to change password page
2. Enter current password correctly
3. Enter new password: `newpass123`
4. Enter confirm password: `different123`
5. Submit

**Expected Results:**
- ✅ Error message: Passwords do not match
- ✅ Password not changed

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

### Test Suite 4: UC-AUTH-004 - Admin Reset Password

#### Test Case 4.1: Valid Password Reset
**Priority:** High  
**Prerequisites:** Logged in as admin

**Steps:**
1. Navigate to `/user`
2. Find `testuser1` in list
3. Click "Reset Password" button
4. Enter new password: `reset123456`
5. Enter confirm password: `reset123456`
6. Submit

**Expected Results:**
- ✅ Success message displayed
- ✅ Logout and login as testuser1 with `reset123456` succeeds
- ✅ Old password fails

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 4.2: Reset Password - Validation Error
**Priority:** Medium

**Steps:**
1. Navigate to user list
2. Click "Reset Password" for a user
3. Enter password: `12345` (too short)
4. Submit

**Expected Results:**
- ✅ Validation error displayed
- ✅ Password not changed

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 4.3: Reset Password - Non-Admin Access
**Priority:** High

**Steps:**
1. Login as staff or manager
2. Try to navigate to `/user?action=resetPassword&userId=1`

**Expected Results:**
- ✅ Access denied
- ✅ 403 error page

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

### Test Suite 5: UC-AUTH-005 - User Logout

#### Test Case 5.1: Successful Logout
**Priority:** High

**Steps:**
1. Login as any user
2. Click "Logout" link/button
3. Observe redirect
4. Try to navigate to protected page (e.g., `/product`)

**Expected Results:**
- ✅ Redirected to login page
- ✅ Success message: "You have been successfully logged out"
- ✅ Attempt to access protected page redirects to login
- ✅ Browser back button doesn't show protected pages

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 5.2: Session Invalidation Check
**Priority:** High

**Steps:**
1. Login as user
2. Note session ID from browser cookies
3. Logout
4. Check browser cookies

**Expected Results:**
- ✅ Session cookie removed or invalidated
- ✅ No user data in session storage

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

### Test Suite 6: UC-AUTH-006 - Session Timeout

#### Test Case 6.1: Session Timeout After Inactivity
**Priority:** High

**Note:** For testing, temporarily modify `web.xml` to set timeout to 1 minute:
```xml
<session-timeout>1</session-timeout>
```

**Steps:**
1. Login as any user
2. Wait for 1 minute without any activity
3. Try to access any protected page

**Expected Results:**
- ✅ Redirected to login page
- ✅ Message: "Your session has expired. Please login again."
- ✅ Yellow/warning alert displayed

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 6.2: Activity Resets Timeout
**Priority:** Medium

**Steps:**
1. Login as user
2. Wait 30 seconds
3. Access a page (refresh or navigate)
4. Wait another 30 seconds
5. Access another page

**Expected Results:**
- ✅ Session remains active
- ✅ No timeout error
- ✅ Pages load normally

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

### Test Suite 7: Security Tests

#### Test Case 7.1: Password Hashing
**Priority:** Critical

**Steps:**
1. Create a new user via registration
2. Check database:
   ```sql
   SELECT PasswordHash FROM Users WHERE Username = 'testuser1';
   ```

**Expected Results:**
- ✅ Password is hashed (not plain text)
- ✅ Format: `salt:hash` (both Base64)
- ✅ Different users have different salts

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 7.2: Role-Based Access Control
**Priority:** High

**Steps:**
1. Login as `staff`
2. Try to access `/user` (admin-only)
3. Try to access `/product` (should work)

**Expected Results:**
- ✅ `/user` shows 403 error
- ✅ `/product` loads successfully

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

#### Test Case 7.3: Public URLs Without Authentication
**Priority:** Medium

**Steps:**
1. Logout (or use incognito window)
2. Try to access:
   - `/auth?action=login`
   - `/assets/css/demo.css`
   - `/favicon.ico`

**Expected Results:**
- ✅ All public URLs load without requiring login
- ✅ No redirect to login page

**Actual Results:** _[Record during testing]_

**Status:** _[Pass/Fail]_

---

## Test Result Recording

### Test Summary Template

| Test Suite | Total Tests | Passed | Failed | Blocked | Pass Rate |
|------------|-------------|--------|--------|---------|-----------|
| UC-AUTH-001 | 10 | | | | |
| UC-AUTH-002 | 8 | | | | |
| UC-AUTH-003 | 5 | | | | |
| UC-AUTH-004 | 3 | | | | |
| UC-AUTH-005 | 2 | | | | |
| UC-AUTH-006 | 2 | | | | |
| Security | 3 | | | | |
| **TOTAL** | **33** | | | | |

### Defect Report Template

| Defect ID | Test Case | Severity | Description | Steps to Reproduce | Expected | Actual | Status |
|-----------|-----------|----------|-------------|-------------------|----------|--------|--------|
| DEF-001 | | | | | | | |

### Severity Levels
- **Critical:** System crash, data loss, security breach
- **High:** Major functionality broken, no workaround
- **Medium:** Major functionality broken, workaround exists
- **Low:** Minor issue, cosmetic problems

---

## Post-Testing Tasks

### After Test Execution
1. ✅ Compile test results
2. ✅ Create defect reports for failures
3. ✅ Update TEST_VERIFICATION_REPORT.md with actual results
4. ✅ Share findings with development team
5. ✅ Schedule bug fixes and retesting

### Regression Testing
After any code changes, re-run:
- All failed test cases
- All related test cases
- Critical path test cases (login, logout, session)

---

## Appendix

### Quick Reference: Test URLs

| Feature | URL |
|---------|-----|
| Login | `/auth?action=login` |
| Logout | `/auth?action=logout` |
| Change Password | `/auth?action=changePassword` |
| User List | `/user` |
| Create User | `/user?action=create` |
| Reset Password | `/user?action=resetPassword&userId=X` |

### Database Queries

**Check User:**
```sql
SELECT * FROM Users WHERE Username = 'admin';
```

**Check Session (if tracked in DB):**
```sql
-- Add if session tracking implemented
```

**Reset Test Data:**
```sql
-- Delete test users
DELETE FROM Users WHERE Username LIKE 'testuser%';
```

---

**Document Version:** 1.0  
**Last Updated:** January 30, 2026  
**Maintained By:** QA Team
