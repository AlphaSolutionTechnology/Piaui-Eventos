# Piauí Events API Documentation

## Overview

This is the official technical documentation for the Piauí Events API. The API is RESTful and designed to efficiently and securely manage events, users, and their respective locations.

## Authentication

The API uses **JWT** with hybrid authentication:
- Default: **HTTP Cookies** (`accessToken`, `refreshToken`, both HttpOnly/Secure/SameSite=Strict)
- Optional: **Authorization Header: Bearer <token>**

Flow:
1. **Login**: sets cookies and also returns the `accessToken`:
   - Header: `Authorization: Bearer <token>` (exposed via `Access-Control-Expose-Headers`)
   - Body: `{ "accessToken": "..." }`
2. **Requests**: send Authorization header (optional). If absent, cookies authenticate automatically.
3. **Refresh**: same as login (updates cookies, header and body with new `accessToken`).

Note: the security filter accepts the Authorization header first; if it doesn't exist, it uses the `accessToken` cookie.

---

## Endpoints

Below are detailed all available endpoints in the API, grouped by functionality.

### 1. Authentication (`/api/auth`)

#### **POST `/api/auth/login`**
Performs login and automatically configures cookies. Also returns the `accessToken` in header and body.
- **Request Body:** `LoginRequestDTO`
- **Success (200 OK):**
  - Headers: `Set-Cookie` (tokens) and `Authorization: Bearer <accessToken>`
  - Body: `{ "message": "...", "accessToken": "..." }`

#### **POST `/api/auth/refresh`**
Renews tokens using the `refreshToken` cookie. Also returns the new `accessToken` in header and body.
- **Success (200 OK):**
  - Headers: updated `Set-Cookie` and `Authorization: Bearer <accessToken>`
  - Body: `{ "message": "...", "accessToken": "..." }`

#### **POST `/api/auth/logout`**
Invalidates authentication cookies.
- **Success (200 OK):** `{ "message": "Logout successful" }`

---

### 2. Event Management (`/api/events`)

#### **GET `/api/events`**
Lists all events registered in the system.
- **Success (200 OK):** List of `EventResponseDTO`.

#### **POST `/api/events`**
Creates a new event.
- **Request Body:** `EventRequestDTO`
- **Success (201 CREATED):** Created `EventResponseDTO`.

#### **GET `/api/events/{id}`**
Returns an event by ID.
- **Path:** `id` (integer)
- **Success (200 OK):** `EventResponseDTO`.

#### **PUT `/api/events/{id}`**
Updates an event.
- **Path:** `id` (integer)
- **Request Body:** `EventRequestDTO`
- **Success (200 OK):** Updated `EventResponseDTO`.

#### **DELETE `/api/events/{id}`**
Removes an event.
- **Path:** `id` (integer)
- **Success (204 NO CONTENT):** No content.

---

### 3. User Management (`/api/user`)

#### **POST `/api/user`**
Creates a new user in the system.
- **Request Body:** `UserRequestDTO`
- **Success (200 OK):** `UserResponseDTO`.

---

### 4. Location Management (`/api/location`)

#### **POST `/api/location`**
Creates a new location for events.
- **Request Body:** `EventLocationDTO`
- **Success (200 OK):** Created `EventLocationDTO`.

#### **GET `/api/location/{cep}`**
Searches address by ZIP code (CEP).
- **Path:** `cep` (string)
- **Success (200 OK):** JSON with address data.

---

## Data Models (DTOs)

### `EventRequestDTO`

| Field         | Type             | Description                                       |
| :------------ | :--------------- | :------------------------------------------------ |
| `name`        | string           | Event name.                                       |
| `description` | string           | Detailed event description.                       |
| `imageUrl`    | string           | Event cover image URL.                            |
| `eventDate`   | string (datetime)| Date/time in ISO 8601 format.                     |
| `eventType`   | string           | Event category.                                   |
| `maxSubs`     | integer          | Maximum number of subscribers.                     |
| `location`    | EventLocationDTO | Location data.                                    |

### `EventResponseDTO`

| Field        | Type              | Description                                  |
| :----------- | :---------------- | :------------------------------------------- |
| `id`         | integer           | Event ID.                                    |
| `name`       | string            | Event name.                                  |
| `description`| string            | Detailed description.                        |
| `imageUrl`   | string            | Image URL.                                   |
| `eventDate`  | string (datetime) | Event date/time.                             |
| `eventType`  | string            | Category.                                    |
| `maxSubs`    | integer           | Maximum subscribers.                         |
| `locationId` | integer           | Associated location ID.                      |
| `version`    | integer           | Record version.                              |

### `UserRequestDTO`

| Field         | Type    | Description                                      |
| :------------ | :------ | :----------------------------------------------- |
| `id`          | integer | User ID (creation: null).                        |
| `name`        | string  | Full name.                                       |
| `email`       | string  | Email (used for login).                          |
| `password`    | string  | Access password.                                 |
| `phoneNumber` | string  | User phone (digits only, with/without 55).      |
| `roleId`      | integer | Access profile/role ID.                         |

### `LoginRequestDTO`

| Field      | Type   | Description                                                                 |
| :--------- | :----- | :-------------------------------------------------------------------------- |
| `username` | string | Email or Brazilian phone number (with or without +55; with/without mask).   |
| `password` | string | User password.                                                              |

---

## Interactive Documentation Access

The API provides interactive documentation via Swagger UI.

### URLs:
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

### How to use (cookies and/or Bearer):
1. Start the application and access Swagger UI
2. Login at the `/api/auth/login` endpoint
3. After login, you can:
   - Use cookies (works automatically); or
   - Click **Authorize** and paste the `accessToken` (without "Bearer" prefix)
4. To refresh, use `/api/auth/refresh` (cookies) and repeat authorization if necessary
5. To logout, use `/api/auth/logout`

Tip: the `accessToken` is also returned in the `Authorization` header and response body.

---

## Running the Application

### Prerequisites:
- Java 17+
- Maven 3.6+
- PostgreSQL (or access to configured database)

### Steps:
1. Configure environment variables (e.g., `DB_PASSWORD`)
2. Run: `./mvnw spring-boot:run`
3. Access: `http://localhost:8081`

---

## Technologies Used

- Spring Boot 3.5.5
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Springdoc OpenAPI
- MapStruct
- Hibernate

---

## CORS Configuration

The API is configured to accept requests from Angular frontend:
- Allowed origins: `http://localhost:4200`, `http://127.0.0.1:4200`
- Allowed methods: GET, POST, PUT, DELETE, PATCH, OPTIONS
- Credentials enabled for cookie-based authentication
- Authorization header exposed for Bearer token access

---

## Login Examples

### Email login:
```json
{
  "username": "user@example.com",
  "password": "mypassword123"
}
```

### Brazilian phone login:
```json
{
  "username": "85999887766",
  "password": "mypassword123"
}
```

### Create user example:
```json
{
  "id": null,
  "name": "John Silva",
  "email": "john.silva@email.com",
  "password": "mypassword123",
  "phoneNumber": "85999887766",
  "roleId": 1
}
```
