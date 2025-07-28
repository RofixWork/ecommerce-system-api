# 🏬 E-Commerce System API

A full-featured backend system for building a professional e-commerce platform using **Spring Boot**. This project
supports:

- ✅ Product management (with images & categories)
- ✅ Cart & order management
- ✅ Reviews & user roles
- ✅ Image uploads to **Cloudinary**
- ✅ Secure authentication using **JWT stored in HTTPOnly cookies**
- ✅ Role-based access control using `@PreAuthorize`
- ✅ Built with layered architecture and best practices

---

## 📁 Project Structure

```

ecommerce-system-api/
├───exception         # Global & custom exception handlers
│   └───base          # Base exceptions
├───response          # Custom global API responses
├───entity            # JPA entities for database tables
│   └───base          # BaseEntity with timestamps
├───controller        # REST Controllers
├───repository        # JPA Repositories
├───dto               # Data Transfer Objects (Request/Response)
│   ├───request
│   └───response
├───service           # Business logic services
├───enums             # Enums (roles, order status, etc.)
├───config            # App config (Swagger, CORS, ModelMapper, etc.)
├───helpers           # Helper utilities
└───security
├───jwt           # JWT logic (provider, filter, etc.)
├───request       # Auth request objects
├───response      # Auth response objects
└───service       # Auth services

````

---

## 🔐 Authentication & Authorization

- Spring Security + JWT-based authentication
- JWT tokens are stored in **HTTPOnly Cookies**
- Uses `@PreAuthorize` for securing endpoints
- Role-based access (Admin / Seller / Customer)

---

## ☁️ Cloudinary Integration

- Product images are uploaded to Cloudinary via API
- Credentials are configured via environment variables

---

## 🛠️ Tech Stack

| Area              | Technology                |
|-------------------|---------------------------|
| Backend Framework | Spring Boot               |
| Security          | Spring Security + JWT     |
| ORM               | Spring Data JPA           |
| Database          | PostgreSQL / MySQL / Neon |
| File Upload       | Cloudinary                |
| Build Tool        | Maven                     |
| API Docs          | Swagger / OpenAPI 3       |
| Containerization  | Docker + JAR Packaging    |

---

## 🌐 Project Repository

🔗 [GitHub Repository](https://github.com/RofixWork/ecommerce-system-api)

---

## ⚙️ Environment Variables

```env
DATABASE_URL=jdbc:xxxxx
DATABASE_USERNAME=xxxxx
DATABASE_PASSWORD=xxxxx

JWT_LIFETIME=900000
JWT_SECRET=xxxxx
JWT_COOKIE=ecommerceCookie
APP_SECURITY_COOKIE_SECURE=false

CLOUDINARY_NAME=xxxxx
CLOUDINARY_API_KEY=xxxxx
CLOUDINARY_SECRET_KEY=xxxxx
````

---

## 🧠 Features

* ✅ Full User Authentication with JWT (stored in cookies)
* ✅ Role management: Admin / Seller / Customer
* ✅ Product listing, filtering, sorting, and pagination
* ✅ Cart and Orders system
* ✅ Review system for products
* ✅ Swagger-generated documentation
* ✅ Clean code structure with layered architecture

---

## 📚 API Documentation

Available at:

```
http://localhost:8080/swagger-ui/index.html
```

Includes live testing, data models, role-based access info.

---

## 📦 Running the Project

```bash
# 1. Build JAR
mvn clean package

# 2. Run with Java
java -jar target/ecommerce-system-api.jar

# 3. Or build Docker image
docker build -t ecommerce-api .
docker run -p 8080:8080 ecommerce-api
```

---

## 📄 Endpoint Overview (Grouped by Feature)

### 🔐 Authentication

| Method | Endpoint             | Access        | Description          |
|--------|----------------------|---------------|----------------------|
| POST   | `/api/auth/register` | Public        | Register new user    |
| POST   | `/api/auth/login`    | Public        | Login and set cookie |
| GET    | `/api/auth/me`       | Authenticated | Get current user     |
| POST   | `/api/auth/logout`   | Authenticated | Logout user          |

---

### 👤 Users (Admin Only)

| Method | Endpoint          | Access | Description       |
|--------|-------------------|--------|-------------------|
| GET    | `/api/users`      | Admin  | Get all users     |
| GET    | `/api/users/{id}` | Admin  | Get specific user |
| DELETE | `/api/users/{id}` | Admin  | Delete a user     |

---

### 🛍️ Products

| Method | Endpoint             | Access       | Description         |
|--------|----------------------|--------------|---------------------|
| GET    | `/api/products`      | Public       | List all products   |
| GET    | `/api/products/{id}` | Public       | Get product details |
| POST   | `/api/products`      | Seller       | Create product      |
| PUT    | `/api/products/{id}` | Seller       | Update product      |
| DELETE | `/api/products/{id}` | Admin/Seller | Delete product      |

---

### 🖼️ Product Images

| Method | Endpoint                              | Access   | Description          |
|--------|---------------------------------------|----------|----------------------|
| POST   | `/api/upload/products/{pId}/image`    | Customer | Upload product image |
| DELETE | `/api/products/{pId}/image/{imageId}` | Admin    | Delete product image |

---

### 🗂️ Categories

| Method | Endpoint               | Access | Description      |
|--------|------------------------|--------|------------------|
| GET    | `/api/categories`      | Public | List categories  |
| POST   | `/api/categories`      | Admin  | Add new category |
| PUT    | `/api/categories/{id}` | Admin  | Update category  |
| DELETE | `/api/categories/{id}` | Admin  | Delete category  |

---

### 🛒 Cart

| Method | Endpoint                       | Access   | Description           |
|--------|--------------------------------|----------|-----------------------|
| GET    | `/api/cart`                    | Customer | Get cart contents     |
| POST   | `/api/cart/add`                | Customer | Add item to cart      |
| DELETE | `/api/cart/remove/{productId}` | Customer | Remove item from cart |
| DELETE | `/api/cart/clear`              | Customer | Clear entire cart     |

---

### 📦 Orders

| Method | Endpoint           | Access   | Description       |
|--------|--------------------|----------|-------------------|
| GET    | `/api/orders`      | Customer | Get all my orders |
| GET    | `/api/orders/{id}` | Customer | Get single order  |
| POST   | `/api/orders`      | Customer | Create new order  |

---

### 🌟 Reviews

| Method | Endpoint                     | Access   | Description          |
|--------|------------------------------|----------|----------------------|
| GET    | `/api/products/{id}/reviews` | Public   | List product reviews |
| POST   | `/api/products/{id}/reviews` | Customer | Add a product review |

---

## 📊 Database Design (ERD Overview)

Includes 10 tables:

* `User`, `Role`, `Product`, `Category`, `ProductImage`
* `Cart`, `CartItem`, `Order`, `OrderItem`, `Review`

✅ Supports relationships like:

* One-to-One: User - Cart
* One-to-Many: User - Product, Product - Image, Order - OrderItem
* Many-to-Many: User - Role
* Many-to-One: Product - Category, Review - Product

---

## 🧰 Best Practices Followed

1. ✅ DTO pattern used — no entity exposed in API
2. ✅ `@PreAuthorize` for role-based access
3. ✅ `@Transactional` for atomic updates
4. ✅ Passwords hashed with `BCryptPasswordEncoder`
5. ✅ All entities extend `BaseEntity` (createdAt / updatedAt)
6. ✅ Pagination, sorting, filtering supported in key endpoints

---

## 🤝 Contributions & Contact

If you like this project or want to contribute, feel free to fork it or open a Pull Request.
For any suggestions or issues, check the [GitHub Repo](https://github.com/RofixWork/ecommerce-system-api).

---

#### ✨ Created with care by **Abdessamad**

