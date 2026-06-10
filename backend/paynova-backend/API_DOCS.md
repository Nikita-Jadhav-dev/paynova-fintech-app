# PayNova Backend — API Reference

## Base URL
```
http://localhost:8080/api
```

## Authentication
All protected endpoints require:
```
Authorization: Bearer <jwt_token>
```

---

## 1. Authentication

### POST /auth/register
```json
// Request
{
  "name": "Riya Sharma",
  "email": "riya@example.com",
  "phone": "9876543210",
  "password": "Secret@123"
}

// Response 201
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": 1,
      "name": "Riya Sharma",
      "email": "riya@example.com",
      "phone": "9876543210",
      "upiId": "9876543210@paynova",
      "role": "USER",
      "status": "ACTIVE",
      "createdAt": "2024-01-15 10:30:00"
    }
  }
}
```

### POST /auth/login
```json
// Request
{
  "email": "riya@example.com",
  "password": "Secret@123"
}

// Response 200 — same shape as register
```

---

## 2. User Profile

### GET /users/me
```json
// Response 200
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "id": 1,
    "name": "Riya Sharma",
    "email": "riya@example.com",
    "phone": "9876543210",
    "upiId": "9876543210@paynova",
    "role": "USER",
    "status": "ACTIVE"
  }
}
```

### PUT /users/me
```json
// Request
{ "name": "Riya S.", "phone": "9876543211" }

// Response 200
{ "success": true, "message": "Profile updated successfully", "data": { ... } }
```

### PUT /users/me/change-password
```json
// Request
{ "currentPassword": "Secret@123", "newPassword": "NewPass@456" }

// Response 200
{ "success": true, "message": "Password changed successfully", "data": null }
```

---

## 3. Wallet

### GET /wallet/balance
```json
// Response 200
{
  "success": true,
  "data": {
    "id": 1,
    "balance": 2500.00,
    "isActive": true,
    "updatedAt": "2024-01-15 11:00:00"
  }
}
```

### POST /wallet/add-money
```json
// Request
{
  "amount": 1000.00,
  "bankAccountId": 2,
  "description": "Top-up from HDFC"
}

// Response 200
{
  "success": true,
  "message": "Money added successfully",
  "data": {
    "transactionId": "TXN-A1B2C3D4E5F6G7H8",
    "amount": 1000.00,
    "type": "ADD_MONEY",
    "status": "SUCCESS",
    "receiverName": "Riya Sharma",
    "createdAt": "2024-01-15 11:05:00",
    "completedAt": "2024-01-15 11:05:00"
  }
}
```

### POST /wallet/send-money
```json
// Request (use any one receiver identifier)
{
  "receiverPhone": "9123456789",
  "amount": 250.00,
  "description": "Dinner split"
}

// Request via UPI ID
{
  "receiverUpiId": "9123456789@paynova",
  "amount": 250.00
}

// Response 200
{
  "success": true,
  "message": "Money sent successfully",
  "data": {
    "transactionId": "TXN-X9Y8Z7W6V5U4T3S2",
    "amount": 250.00,
    "type": "SEND",
    "status": "SUCCESS",
    "senderName": "Riya Sharma",
    "senderPhone": "9876543210",
    "receiverName": "Arjun Mehta",
    "receiverPhone": "9123456789",
    "description": "Dinner split",
    "createdAt": "2024-01-15 11:10:00",
    "completedAt": "2024-01-15 11:10:00"
  }
}
```

---

## 4. Bank Accounts

### POST /bank-accounts
```json
// Request
{
  "accountNumber": "123456789012",
  "ifscCode": "HDFC0001234",
  "bankName": "HDFC Bank",
  "accountHolderName": "Riya Sharma",
  "isPrimary": true
}

// Response 201
{
  "success": true,
  "message": "Bank account added successfully",
  "data": {
    "id": 1,
    "accountNumber": "xxxxxxxx9012",
    "ifscCode": "HDFC0001234",
    "bankName": "HDFC Bank",
    "accountHolderName": "Riya Sharma",
    "balance": 0.00,
    "isPrimary": true,
    "isVerified": false
  }
}
```

### GET /bank-accounts
```json
// Response 200
{
  "success": true,
  "data": [
    {
      "id": 1,
      "accountNumber": "xxxxxxxx9012",
      "bankName": "HDFC Bank",
      "isPrimary": true,
      "isVerified": false
    }
  ]
}
```

### PATCH /bank-accounts/{id}/set-primary
### DELETE /bank-accounts/{id}

---

## 5. Transactions

### GET /transactions?page=0&size=20
```json
// Response 200
{
  "success": true,
  "data": {
    "content": [
      {
        "transactionId": "TXN-A1B2C3D4E5F6G7H8",
        "amount": 1000.00,
        "type": "ADD_MONEY",
        "status": "SUCCESS",
        "createdAt": "2024-01-15 11:05:00"
      }
    ],
    "page": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true
  }
}
```

### GET /transactions/filter?type=SEND&page=0&size=20
### GET /transactions/{transactionId}

### POST /transactions/qr-pay
```json
// Request
{
  "qrCode": "QR-A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5",
  "amount": 500.00,
  "description": "Coffee"
}

// Response 200
{
  "success": true,
  "message": "QR payment successful",
  "data": {
    "transactionId": "TXN-Q1R2S3T4U5V6W7X8",
    "amount": 500.00,
    "type": "QR_PAYMENT",
    "status": "SUCCESS",
    "senderName": "Riya Sharma",
    "receiverName": "Cafe Store"
  }
}
```

---

## 6. QR Code

### GET /qr/my-code
```json
// Response 200
{
  "success": true,
  "data": {
    "id": 1,
    "qrCode": "QR-A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5",
    "qrImageUrl": "/api/qr/image/QR-A1B2...",
    "isActive": true
  }
}
```

### POST /qr/regenerate
### GET /qr/lookup/{qrCode}

---

## 7. Admin Endpoints (ROLE_ADMIN only)

### GET /admin/stats
```json
// Response 200
{
  "success": true,
  "data": {
    "totalUsers": 1523,
    "activeUsers": 1498,
    "blockedUsers": 25,
    "totalTransactions": 48200,
    "successfulTransactions": 47800,
    "failedTransactions": 400,
    "totalMoneyTransferred": 9850000.00,
    "totalWalletBalance": 2350000.00,
    "transactionsToday": 328,
    "newUsersThisMonth": 87
  }
}
```

### GET  /admin/users?page=0&size=20
### GET  /admin/users/search?query=riya
### GET  /admin/users/{userId}
### PATCH /admin/users/{userId}/block
### PATCH /admin/users/{userId}/unblock
### DELETE /admin/users/{userId}
### GET  /admin/transactions?page=0&size=20
### GET  /admin/transactions/{transactionId}

### POST /admin/transactions/refund
```json
// Request
{
  "transactionId": "TXN-X9Y8Z7W6V5U4T3S2",
  "reason": "Duplicate charge reported by user"
}

// Response 200
{
  "success": true,
  "message": "Transaction refunded successfully",
  "data": {
    "transactionId": "TXN-REFUND123456789",
    "amount": 250.00,
    "type": "REFUND",
    "status": "SUCCESS",
    "description": "Refund for transaction: TXN-X9Y8Z7W6V5U4T3S2 — Duplicate charge reported by user"
  }
}
```

---

## Error Responses

All errors follow the same envelope:
```json
// 400 Validation error
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "amount": "Minimum transfer amount is ₹1",
    "phone": "Invalid Indian phone number"
  }
}

// 401 Unauthorized
{ "success": false, "message": "Unauthorized: Full authentication is required" }

// 403 Forbidden
{ "success": false, "message": "Access denied: you don't have permission to perform this action" }

// 404 Not Found
{ "success": false, "message": "User not found with id: '99'" }

// 409 Conflict
{ "success": false, "message": "Email is already registered: riya@example.com" }

// 500 Internal Server Error
{ "success": false, "message": "An unexpected error occurred. Please try again later." }
```

---

## Swagger UI
```
http://localhost:8080/api/swagger-ui.html
```

## Default Admin Credentials
```
Email:    admin@paynova.com
Password: Admin@1234
```
