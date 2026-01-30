# UC-AUTH-002: User Registration

## Use Case Overview
| Attribute | Description |
|-----------|-------------|
| **Use Case ID** | UC-AUTH-002 |
| **Use Case Name** | User Registration |
| **Primary Actor** | New User (Unregistered) |
| **Description** | Register a new user account with email verification |
| **Preconditions** | User does not have an existing account |
| **Postconditions** | New user account is created with pending verification status |

---

## Main Flow

### Step 1: Display Registration Form
- System displays registration form with fields:
  - Username (text input, required)
  - Email (email input, required)
  - Password (password input, required)
  - Confirm Password (password input, required)
  - Submit button

### Step 2: User Submits Registration Data
- User fills in all required fields
- User clicks submit button

### Step 3: Validate Input Fields
- **Validation Rules:**
  - Username: required, alphanumeric, 3-50 characters
  - Email: required, valid email format
  - Password: required, minimum 6 characters
  - Confirm Password: must match Password
- If validation fails → **Alternative Flow A1**

### Step 4: Check Username Availability
- System checks if username already exists in database
- If username exists → **Alternative Flow A2**

### Step 5: Check Email Availability
- System checks if email already exists in database
- If email exists → **Alternative Flow A3**

### Step 6: Generate Password Hash
- System generates random salt (16 bytes)
- System computes SHA-256 hash of (password + salt)
- System stores hash and salt (never plain password)

### Step 7: Create User Account
- System creates new user record with:
  - Username
  - Email
  - Password Hash
  - Salt
  - Role: (default role or as assigned)
  - Status: Active (or Pending if email verification required)
  - Created timestamp
  - Last Login: null

### Step 8: Send Verification Email (if applicable)
- System generates unique verification token
- System sends email with verification link
- System stores token with expiration (24 hours)

### Step 9: Display Confirmation
- System displays success message: "Registration successful. Please check your email to verify your account."
- System redirects to login page

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
  2. System highlights invalid fields
  3. Return to Step 1 (form retains valid entries)

### A2: Username Already Exists
- **Trigger:** Username is already registered
- **Steps:**
  1. System displays error message: "Username is already taken. Please choose another."
  2. System clears username field
  3. Return to Step 1 (retain other valid entries)

### A3: Email Already Registered
- **Trigger:** Email is already associated with an account
- **Steps:**
  1. System displays error message: "Email is already registered. Please use a different email or try logging in."
  2. System clears email field
  3. Return to Step 1 (retain other valid entries)

---

## Business Rules
| Rule ID | Description |
|---------|-------------|
| BR-REG-001 | Password must be minimum 6 characters |
| BR-REG-002 | Password must be hashed with SHA-256 and random salt |
| BR-REG-003 | Each username must be unique in the system |
| BR-REG-004 | Each email must be unique in the system |
| BR-REG-005 | New accounts may require email verification before activation |

---

## UI Requirements
- Registration form centered on page
- Real-time validation feedback preferred
- Password strength indicator (optional)
- Clear error message display
- Link to login page for existing users
- Terms and conditions checkbox (if applicable)

---

## Security Considerations
- Never store plain text passwords
- Generate cryptographically secure random salt
- Validate email format on both client and server side
- Implement CAPTCHA to prevent automated registrations
- Rate limit registration attempts from same IP
