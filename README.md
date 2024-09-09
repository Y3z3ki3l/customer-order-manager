### Capstone Project Template: Microservice API with Cloud Deployment

This capstone project will guide you through creating a microservice API from scratch, demonstrating your skills in Java functional programming, reactive programming, and modern Java development practices. It will include cloud deployment, API design, testing using JUnit 5 and Mockito, and integration of key Java features like Streams, Lambdas, Generics, and RxJava.

---

### **Project Overview:**

**Project Name**: `Customer Order Microservice`

**Objective**: Build a RESTful API that manages customer orders using Java (Spring Boot), functional and reactive programming, and cloud deployment. The microservice will expose REST endpoints to create, read, update, and delete customer orders. You will also handle asynchronous communication using RxJava, demonstrate handling `Optional` to avoid null pointers, and use streams and lambda functions for data processing.

**Cloud Deployment**: Deploy the microservice to a cloud provider such as AWS, GCP, or Azure using Docker or Kubernetes.

---

### **High-Level Architecture**

The architecture will follow a microservice pattern:
1. **API Layer**: Expose RESTful endpoints.
2. **Service Layer**: Implement the business logic using functional and reactive programming.
3. **Data Access Layer**: Use in-memory databases or cloud-hosted databases (e.g., AWS RDS) to persist data.
4. **Cloud Deployment**: Package the app using Docker, deploy to Kubernetes or directly to AWS ECS/Elastic Beanstalk.

---

### **Technologies & Tools:**

- **Backend Framework**: Spring Boot (Java 17 or 11)
- **Reactive Programming**: RxJava 3 or Reactor (Spring WebFlux)
- **Functional Programming**: Java Streams, Lambdas, Optionals, Method References
- **Unit Testing**: JUnit 5, Mockito
- **API Documentation**: Swagger/OpenAPI
- **Database**: H2 (for local development), Postgres or MySQL (cloud)
- **Cloud Deployment**: AWS, Azure, or GCP (with Docker and Kubernetes)
- **Continuous Integration**: GitHub Actions, CircleCI, or Jenkins

---

### **Functional Requirements:**

1. **Customer API**:
   - Create a customer
   - Retrieve a customer by ID
   - Retrieve all customers
   - Update a customer
   - Delete a customer

2. **Order API**:
   - Create an order for a customer
   - Retrieve an order by ID
   - Retrieve all orders for a customer
   - Update an order
   - Delete an order

---

### **Project Structure:**

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.microservice
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       ├── entity
│   │   │       ├── repository
│   │   │       ├── service
│   │   │       └── utils
│   └── test
│       ├── java
│       │   └── com.example.microservice
│       │       ├── controller
│       │       ├── service
│       │       └── repository
├── Dockerfile
├── application.yml
└── pom.xml
```

---

### **Step-by-Step Implementation**

#### 1. **Create Spring Boot Project**

Create a Spring Boot project using Spring Initializr with dependencies:
- Spring Web (for REST API)
- Spring Data JPA (for database access)
- H2 Database (for local dev)
- RxJava or Reactor (for reactive programming)
- Lombok (for boilerplate reduction)

#### 2. **Define Entities and DTOs**

- **Customer Entity**
```java
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    // Constructors, Getters, Setters
}
```

- **Order Entity**
```java
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String product;
    private Integer quantity;

    @ManyToOne
    private Customer customer;

    // Constructors, Getters, Setters
}
```

- **DTOs** (e.g., `CustomerDto`, `OrderDto`) will map entity data to response format.

#### 3. **Create Repositories**

- **CustomerRepository**
```java
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
```

- **OrderRepository**
```java
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
}
```

#### 4. **Service Layer (Functional Programming)**

- **CustomerService**: Use functional interfaces, streams, and optionals to handle logic.

```java
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll().stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
```

#### 5. **Controller Layer**

- **CustomerController**

```java
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Customer>> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}
```

#### 6. **RxJava in Service Layer (Reactive Programming)**

```java
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Observable<Order> getOrderById(Long id) {
        return Observable.fromCallable(() -> orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found")));
    }

    public Observable<List<Order>> getOrdersByCustomer(Long customerId) {
        return Observable.fromCallable(() -> orderRepository.findByCustomerId(customerId));
    }
}
```

#### 7. **Testing with JUnit 5 and Mockito**

- **Unit Test for `CustomerService`** using Mockito.

```java
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testGetCustomerById() {
        Customer customer = new Customer(1L, "John Doe", "john.doe@example.com");
        Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerService.getCustomerById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }
}
```

#### 8. **Cloud Deployment (Docker and Kubernetes)**

- **Dockerfile** for containerizing the application:

```dockerfile
FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/customer-order-microservice.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

- **Kubernetes Deployment YAML**:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: customer-order-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: customer-order-service
  template:
    metadata:
      labels:
        app: customer-order-service
    spec:
      containers:
        - name: customer-order-service
          image: your-docker-image:latest
          ports:
            - containerPort: 8080
---
apiVersion: v1
kind: Service
metadata:
  name: customer-order-service
spec:
  type: LoadBalancer
  ports:
    - port: 80
      targetPort: 8080
  selector:
    app: customer-order-service
```

#### 9. **CI/CD Pipeline**

- Use GitHub Actions for automated testing, Docker builds, and pushing to a cloud registry (AWS ECR, Docker Hub).
- Example workflow for GitHub Actions:

```yaml
name: Java CI with Maven

on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v2
    - name: Set up JDK 17
      uses: actions/setup-java@v1
      with:
        java-version: '17'
    - name: Build with Maven
      run: mvn clean install
    - name: Run Unit Tests
      run: mvn test
    - name: Build Docker Image
      run: docker build . -t your-docker-repo/customer-order-service:latest
    - name: Push to Docker Hub
      run: docker push your-docker-repo/customer-order-service:latest
```

---

### **Conclusion**

By completing this capstone project, you will demonstrate mastery in key Java and software development topics:
- Functional and reactive programming with Java Streams, Lambdas, and RxJava.
- Building scalable REST APIs using Spring Boot.
- Cloud deployment using Docker and Kubernetes.
- Writing unit

 tests with JUnit 5 and Mockito.
- Implementing best practices in modern Java development.

This project will serve as a comprehensive showcase of your skills and readiness for real-world Java software engineering challenges.
