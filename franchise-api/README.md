# Franchise API

RESTful API built with Spring Boot to manage franchises, their branches, and branch products backed by MongoDB storage.

## Requirements

- Java 17+
- Maven 3.9+
- MongoDB instance (local or Atlas cluster)

## Configuration

The application is preconfigured to connect to the provided MongoDB Atlas cluster:

```
mongodb+srv://jcleon892_db_user:DrMRLdoYAgEpW3XU@cluster0.lsvnmvi.mongodb.net/franchise-api?retryWrites=true&w=majority
```

If you need to point the API to a different database, set the `SPRING_DATA_MONGODB_URI` environment variable with your own connection string before starting the application.

## Running locally

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Building the project

```bash
./mvnw clean package
```

## Docker

Build the container image after packaging the application:

```bash
./mvnw -DskipTests package
docker build -t franchise-api .
```

Run the container (replace the URI with your own):

```bash
docker run -e SPRING_DATA_MONGODB_URI="mongodb+srv://<user>:<password>@<cluster-host>/<database>?retryWrites=true&w=majority" -p 8080:8080 franchise-api
```

## API Endpoints

| Method | Path | Description |
| --- | --- | --- |
| POST | `/api/franchises` | Create a new franchise. |
| PUT | `/api/franchises/{franchiseId}/name` | Update a franchise name. |
| POST | `/api/franchises/{franchiseId}/branches` | Add a new branch to a franchise. |
| PUT | `/api/franchises/{franchiseId}/branches/{branchId}/name` | Update a branch name. |
| POST | `/api/franchises/{franchiseId}/branches/{branchId}/products` | Add a new product to a branch. |
| DELETE | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}` | Remove a product from a branch. |
| PUT | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock` | Update a product stock. |
| PUT | `/api/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name` | Update a product name. |
| GET | `/api/franchises/{franchiseId}/branches/top-products` | List the products with the highest stock per branch for a franchise. |

### Examples

#### Create a franchise

```http
POST /api/franchises
Content-Type: application/json

{
  "name": "Coffee Lovers"
}
```

Response

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": "665b90f5bf2c6369c9dd9c01",
  "name": "Coffee Lovers",
  "branches": []
}
```

#### Update a franchise name

```http
PUT /api/franchises/665b90f5bf2c6369c9dd9c01/name
Content-Type: application/json

{
  "name": "Coffee Lovers International"
}
```

#### Add a branch to a franchise

```http
POST /api/franchises/665b90f5bf2c6369c9dd9c01/branches
Content-Type: application/json

{
  "name": "Downtown"
}
```

Response

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": "665b910bbf2c6369c9dd9c02",
  "name": "Downtown",
  "products": []
}
```

#### Update a branch name

```http
PUT /api/franchises/665b90f5bf2c6369c9dd9c01/branches/665b910bbf2c6369c9dd9c02/name
Content-Type: application/json

{
  "name": "Downtown Flagship"
}
```

#### Add a product to a branch

```http
POST /api/franchises/665b90f5bf2c6369c9dd9c01/branches/665b910bbf2c6369c9dd9c02/products
Content-Type: application/json

{
  "name": "Espresso Beans",
  "stock": 120
}
```

Response

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": "665b9152bf2c6369c9dd9c03",
  "name": "Espresso Beans",
  "stock": 120
}
```

#### Update a product stock

```http
PUT /api/franchises/665b90f5bf2c6369c9dd9c01/branches/665b910bbf2c6369c9dd9c02/products/665b9152bf2c6369c9dd9c03/stock
Content-Type: application/json

{
  "stock": 150
}
```

#### Update a product name

```http
PUT /api/franchises/665b90f5bf2c6369c9dd9c01/branches/665b910bbf2c6369c9dd9c02/products/665b9152bf2c6369c9dd9c03/name
Content-Type: application/json

{
  "name": "Organic Espresso Beans"
}
```

#### Remove a product from a branch

```http
DELETE /api/franchises/665b90f5bf2c6369c9dd9c01/branches/665b910bbf2c6369c9dd9c02/products/665b9152bf2c6369c9dd9c03
```

Response

```http
HTTP/1.1 204 No Content
```

#### List top-stock products per branch

```http
GET /api/franchises/665b90f5bf2c6369c9dd9c01/branches/top-products
```

Response

```http
HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "branchId": "665b910bbf2c6369c9dd9c02",
    "branchName": "Downtown Flagship",
    "product": {
      "id": "665b9152bf2c6369c9dd9c03",
      "name": "Organic Espresso Beans",
      "stock": 150
    }
  }
]
```

## Testing

```bash
./mvnw test
```

## Notes

- Validation errors return HTTP 400 with details per field.
- Not found resources respond with HTTP 404.
