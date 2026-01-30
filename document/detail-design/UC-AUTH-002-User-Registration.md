# UC-AUTH-002: User Registration

## Use Case Overview
| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-AUTH-002 |
| **Use Case Name** | User Registration |
| **Primary Actor** | Admin |
| **Description** | Admin creates a new user account in the system |
| **Preconditions** | Admin is logged in; Username and email are not already registered |
| **Postconditions** | New user account is created with Active status |

---

## Main Flow

### Step 1: Navigate to User Management
- Admin navigates to User Management section
- System displays list of existing users

### Step 2: Initiate New User Creation
- Admin clicks "Create New User" button
- System displays user registration form

### Step 3: Display Registration Form
- System displays registration form with fields:
  - Username (text input, required)
  - Email (email input, required)
  - Password (password input, required)
  - Confirm Password (password input, required)
  - Role (dropdown, required): Admin, Manager, Staff, Sales
  - Status (dropdown, default: Active)
  - Submit button

### Step 4: Admin Submits User Data
- Admin fills in all required fields
- Admin selects appropriate role for the new user
- Admin clicks submit button

### Step 5: Validate Input Fields
- **Validation Rules:**
  - Username: required, alphanumeric, 3-50 characters
  - Email: required, valid email format
  - Password: required, minimum 6 characters
  - Confirm Password: must match Password
  - Role: required, must be valid role
- If validation fails → **Alternative Flow A1**

### Step 6: Check Username Availability
- System checks if username already exists in database
- If username exists → **Alternative Flow A2**

### Step 7: Check Email Availability
- System checks if email already exists in database
- If email exists → **Alternative Flow A3**

### Step 8: Generate Password Hash
- System generates random salt (16 bytes)
- System computes SHA-256 hash of (password + salt)
- System stores hash and salt (never plain password)

### Step 9: Create User Account
- System creates new user record with:
  - Username
  - Email
  - Password Hash
  - Salt
  - Role: As selected by Admin
  - Status: Active
  - Created By: Admin's User ID
  - Created timestamp
  - Last Login: null

### Step 10: Send Welcome Email (Optional)
- System sends email to new user with:
  - Welcome message
  - Temporary password
  - Instructions to change password on first login
  - Login URL

### Step 11: Display Confirmation
- System displays success message: "User [username] created successfully with role [role]"
- System redirects to User Management list or user detail page

---

## Alternative Flows

### A1: Input Validation Failed
- **Trigger:** One or more validation rules failed
- **Steps:**
  1. System displays specific error messages for each failed validation:
     - "Username is required and must be 3-50 alphanumeric characters"
     - "Please enter a valid email address"
     - "Password must be at least 6 characters"
     - "Passwords do not match"
     - "Please select a valid role"
  2. System highlights invalid fields
  3. Return to Step 3 (form retains valid entries)

### A2: Username Already Exists
- **Trigger:** Username is already registered
- **Steps:**
  1. System displays error message: "Username is already taken. Please choose another."
  2. System clears username field
  3. Return to Step 3 (retain other valid entries)

### A3: Email Already Registered
- **Trigger:** Email is already associated with an account
- **Steps:**
  1. System displays error message: "Email is already registered. Please use a different email."
  2. System clears email field
  3. Return to Step 3 (retain other valid entries)

---

## Business Rules
| Rule ID | Description |
|---------|-------------|
| BR-REG-001 | Only Admin can create new user accounts |
| BR-REG-002 | Password must be minimum 6 characters |
| BR-REG-003 | Password must be hashed with SHA-256 and random salt |
| BR-REG-004 | Each username must be unique in the system |
| BR-REG-005 | Each email must be unique in the system |
| BR-REG-006 | Admin must assign a role during user creation |
| BR-REG-007 | New accounts are created with Active status by default |

---

## Access Control
| Role | Permission |
|------|------------|
| Admin | ✓ Can create new users |
| Manager | ✗ Cannot create users |
| Staff | ✗ Cannot create users |
| Sales | ✗ Cannot create users |

---

## UI Requirements
- Accessible from User Management section
- Clear form with all required fields
- Role dropdown with all available roles
- Status dropdown (Active/Inactive)
- Real-time validation feedback
- Password strength indicator (optional)
- Clear error message display
- Cancel button to return to User Management list
- Option to send welcome email to new user


