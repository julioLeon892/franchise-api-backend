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

## Testing

```bash
./mvnw test
```

## Notes

- Validation errors return HTTP 400 with details per field.
- Not found resources respond with HTTP 404.
