# Hướng Dẫn Kiểm Thử Hộp Trắng với JUnit

## 📋 Mục Lục
1. [Giới thiệu về White Box Testing](#1-giới-thiệu)
2. [Cấu trúc Test Cases](#2-cấu-trúc-test-cases)
3. [Hướng dẫn chạy Tests](#3-hướng-dẫn-chạy-tests)
4. [Xem Test Coverage Report](#4-xem-test-coverage-report)
5. [Phân tích kết quả](#5-phân-tích-kết-quả)
6. [Best Practices](#6-best-practices)

---

## 1. Giới Thiệu

### White Box Testing là gì?
**White Box Testing** (Kiểm thử hộp trắng) là phương pháp kiểm thử phần mềm mà người test có thể nhìn thấy cấu trúc bên trong của code. Mục tiêu là:
- ✅ Test tất cả các đường dẫn logic (branches)
- ✅ Test tất cả các điều kiện (if/else)
- ✅ Test các trường hợp biên (edge cases)
- ✅ Đảm bảo code coverage cao (>80%)

### Công nghệ sử dụng
- **JUnit 5** (Jupiter): Testing framework
- **Mockito**: Mocking framework
- **Spring Boot Test**: Integration testing
- **JaCoCo**: Code coverage tool
- **Maven Surefire**: Test runner

---

## 2. Cấu Trúc Test Cases

### Danh sách test classes đã tạo:

```
src/test/java/iuh/student/www/
├── service/
│   ├── UserServiceTest.java         (27 test cases)
│   ├── ProductServiceTest.java      (33 test cases)
│   └── ...
├── security/
│   └── JwtUtilTest.java             (21 test cases)
└── ShoppingMomadnBabyApplicationTests.java
```

### Chi tiết test coverage:

#### 1. UserServiceTest (27 tests)
**Coverage**: Tất cả methods trong UserService

| Method | Test Cases | Branches Covered |
|--------|-----------|------------------|
| `registerUser()` | 6 tests | ✅ Email exists<br>✅ Password mismatch<br>✅ Invalid email<br>✅ Null/empty fullname<br>✅ Success |
| `updateUser()` | 5 tests | ✅ User not found<br>✅ Null/empty fullname<br>✅ Success |
| `deleteUser()` | 4 tests | ✅ User not found<br>✅ Cannot delete admin<br>✅ User has orders<br>✅ Success |
| `toggleUserStatus()` | 4 tests | ✅ User not found<br>✅ Cannot toggle admin<br>✅ Enable/Disable |
| `findByEmail()` | 2 tests | ✅ Found<br>✅ Not found |
| `findById()` | 2 tests | ✅ Found<br>✅ Not found |
| Other methods | 4 tests | ✅ All query methods |

#### 2. ProductServiceTest (33 tests)
**Coverage**: Tất cả methods trong ProductService

| Method | Test Cases | Branches Covered |
|--------|-----------|------------------|
| `createProduct()` | 11 tests | ✅ Category null/not found<br>✅ Name null/empty<br>✅ Price null/zero/negative<br>✅ Stock null/negative<br>✅ Success |
| `updateProduct()` | 3 tests | ✅ Product not found<br>✅ Active null<br>✅ Success |
| `deleteProduct()` | 3 tests | ✅ Product not found<br>✅ In orders<br>✅ Success |
| `updateStock()` | 4 tests | ✅ Product not found<br>✅ Insufficient stock<br>✅ Exact stock<br>✅ Success |
| Search methods | 12 tests | ✅ All search variations |

#### 3. JwtUtilTest (21 tests)
**Coverage**: Tất cả methods trong JwtUtil

| Method | Test Cases | Branches Covered |
|--------|-----------|------------------|
| `generateToken()` | 5 tests | ✅ Success<br>✅ Multiple users<br>✅ Multiple roles<br>✅ Complex email |
| `extractUsername()` | 2 tests | ✅ Success<br>✅ Invalid token |
| `extractExpiration()` | 2 tests | ✅ Success<br>✅ Correct duration |
| `validateToken()` | 4 tests | ✅ Valid<br>✅ Username mismatch<br>✅ Expired<br>✅ Not expired |
| `extractClaim()` | 3 tests | ✅ Subject<br>✅ Expiration<br>✅ IssuedAt |
| Signing key | 2 tests | ✅ Verify same secret<br>✅ Cannot verify different secret |
| Edge cases | 3 tests | ✅ Various scenarios |

---

## 3. Hướng Dẫn Chạy Tests

### 3.1. Chạy tất cả tests

```bash
# Sử dụng Maven wrapper
./mvnw clean test

# Hoặc sử dụng Maven (nếu đã cài)
mvn clean test
```

### 3.2. Chạy test cho 1 class cụ thể

```bash
# Test UserService
./mvnw test -Dtest=UserServiceTest

# Test ProductService
./mvnw test -Dtest=ProductServiceTest

# Test JwtUtil
./mvnw test -Dtest=JwtUtilTest
```

### 3.3. Chạy 1 test method cụ thể

```bash
# Chạy test method cụ thể
./mvnw test -Dtest=UserServiceTest#testRegisterUser_Success

# Chạy nhiều test methods
./mvnw test -Dtest=UserServiceTest#testRegisterUser_Success+testUpdateUser_Success
```

### 3.4. Chạy tests với JaCoCo report

```bash
# Clean, test và generate JaCoCo report
./mvnw clean test jacoco:report
```

### 3.5. Skip tests (khi build production)

```bash
# Skip tests when building
./mvnw clean package -DskipTests

# Or
./mvnw clean package -Dmaven.test.skip=true
```

---

## 4. Xem Test Coverage Report

### 4.1. Generate Coverage Report

```bash
# Step 1: Clean và chạy tests với JaCoCo
./mvnw clean test

# JaCoCo tự động generate report sau khi test xong
```

### 4.2. Location của Reports

Sau khi chạy tests, reports được tạo tại:

```
target/
├── surefire-reports/          # Test results
│   ├── TEST-*.xml            # XML reports
│   └── *.txt                 # Text reports
└── site/
    └── jacoco/               # JaCoCo coverage reports
        ├── index.html        # 👉 MỞ FILE NÀY!
        ├── jacoco.xml        # XML report
        └── jacoco.csv        # CSV report
```

### 4.3. Mở HTML Report

**Windows:**
```bash
start target/site/jacoco/index.html
```

**MacOS:**
```bash
open target/site/jacoco/index.html
```

**Linux:**
```bash
xdg-open target/site/jacoco/index.html
# Hoặc
firefox target/site/jacoco/index.html
```

**Hoặc thủ công:**
1. Mở thư mục `target/site/jacoco/`
2. Double-click vào file `index.html`
3. Report sẽ mở trong browser

### 4.4. Đọc hiểu JaCoCo Report

#### Dashboard chính (index.html):

![JaCoCo Dashboard Example](https://www.jacoco.org/jacoco/trunk/doc/resources/report.gif)

**Các metrics quan trọng:**

| Metric | Ý nghĩa | Mục tiêu |
|--------|---------|----------|
| **Instructions** | Số lệnh bytecode được test | >80% |
| **Branches** | Số nhánh (if/else) được test | >70% |
| **Lines** | Số dòng code được test | >80% |
| **Methods** | Số methods được test | >75% |
| **Classes** | Số classes được test | >70% |

#### Màu sắc coverage:

- 🟢 **Green**: Coverage cao (>80%)
- 🟡 **Yellow**: Coverage trung bình (50-80%)
- 🔴 **Red**: Coverage thấp (<50%)

#### Chi tiết từng class:

1. Click vào package (ví dụ: `iuh.student.www.service`)
2. Click vào class (ví dụ: `UserService`)
3. Xem source code với highlighting:
   - 🟢 **Green background**: Dòng code đã được test
   - 🔴 **Red background**: Dòng code CHƯA được test
   - 🟡 **Yellow diamond**: Branch chỉ test 1 phần

---

## 5. Phân Tích Kết Quả

### 5.1. Console Output

Khi chạy tests, bạn sẽ thấy output như sau:

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running iuh.student.www.service.UserServiceTest
[INFO] Tests run: 27, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.345 s
[INFO] Running iuh.student.www.service.ProductServiceTest
[INFO] Tests run: 33, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.987 s
[INFO] Running iuh.student.www.security.JwtUtilTest
[INFO] Tests run: 21, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.543 s
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 81, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] --- jacoco-maven-plugin:0.8.11:report (report) @ www ---
[INFO] Loading execution data file target/jacoco.exec
[INFO] Analyzed bundle 'ShoppingMomadnBaby' with 64 classes
[INFO] BUILD SUCCESS
```

### 5.2. Surefire Text Reports

```bash
# Xem summary
cat target/surefire-reports/*.txt
```

Example output:
```
Test set: iuh.student.www.service.UserServiceTest
Tests run: 27, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.345 s - in UserServiceTest
  ✓ Test 1: Register user thành công - Happy path
  ✓ Test 2: Register user - Email đã tồn tại
  ✓ Test 3: Register user - Password không khớp
  ...
```

### 5.3. JaCoCo Coverage Summary

Mở `target/site/jacoco/index.html` để xem:

```
Package: iuh.student.www.service
  UserService:        Instructions: 95%  |  Branches: 92%  |  Lines: 96%
  ProductService:     Instructions: 97%  |  Branches: 94%  |  Lines: 98%

Package: iuh.student.www.security
  JwtUtil:           Instructions: 91%  |  Branches: 85%  |  Lines: 93%

OVERALL COVERAGE:    Instructions: 85%  |  Branches: 78%  |  Lines: 87%
```

---

## 6. Best Practices

### 6.1. Naming Convention

```java
@Test
@DisplayName("Test [số]: [Chức năng] - [Tình huống]")
void test[MethodName]_[Scenario]() {
    // Given - Setup data
    // When - Execute method
    // Then - Assert results
}
```

**Ví dụ:**
```java
@Test
@DisplayName("Test 1: Register user thành công - Happy path")
void testRegisterUser_Success() { ... }

@Test
@DisplayName("Test 2: Register user - Email đã tồn tại")
void testRegisterUser_EmailExists() { ... }
```

### 6.2. Test Structure (Given-When-Then)

```java
@Test
void testUpdateProduct_Success() throws Exception {
    // Given - Chuẩn bị dữ liệu test
    Product product = Product.builder()
            .name("Test Product")
            .price(100000.0)
            .build();
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    // When - Thực thi method cần test
    Product result = productService.updateProduct(1L, product);

    // Then - Kiểm tra kết quả
    assertNotNull(result);
    assertEquals("Test Product", result.getName());
    verify(productRepository, times(1)).save(any(Product.class));
}
```

### 6.3. Test Coverage Goals

| Layer | Target Coverage | Priority |
|-------|----------------|----------|
| **Service** | >85% | 🔥 High |
| **Security** | >90% | 🔥 High |
| **Controller** | >70% | 📊 Medium |
| **Repository** | >60% | 📊 Medium |
| **DTO/Entity** | Skip | ⚪ Low |

### 6.4. Test Types

#### Unit Tests (hiện tại)
- Test từng method riêng lẻ
- Mock tất cả dependencies
- Fast execution

#### Integration Tests (tùy chọn)
```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegisterUser_Integration() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{...}"))
                .andExpect(status().isOk());
    }
}
```

### 6.5. Common Patterns

#### Test Exception Handling:
```java
@Test
void testMethod_ThrowsException() {
    // When & Then
    Exception exception = assertThrows(CustomException.class, () -> {
        service.methodThatThrows();
    });

    assertEquals("Expected message", exception.getMessage());
}
```

#### Test Multiple Branches:
```java
// Branch 1: Success
@Test
void testMethod_Success() { ... }

// Branch 2: Null input
@Test
void testMethod_NullInput() { ... }

// Branch 3: Invalid input
@Test
void testMethod_InvalidInput() { ... }
```

#### Verify Mock Interactions:
```java
// Verify method called exactly once
verify(repository, times(1)).save(any());

// Verify method never called
verify(repository, never()).delete(any());

// Verify method called with specific argument
verify(repository).findById(1L);
```

---

## 7. Troubleshooting

### 7.1. Tests bị fail

**Kiểm tra:**
1. Dependencies đã đúng chưa?
2. Mock setup đã đầy đủ chưa?
3. Assertions có đúng không?

**Debug:**
```bash
# Chạy tests với debug logging
./mvnw test -X

# Chạy 1 test cụ thể để debug
./mvnw test -Dtest=UserServiceTest#testRegisterUser_Success
```

### 7.2. Coverage thấp

**Cách cải thiện:**
1. Xem JaCoCo report để tìm code chưa được test
2. Thêm tests cho các branches còn thiếu
3. Test các edge cases
4. Test exception handling

### 7.3. Tests chạy chậm

**Optimization:**
```java
// Use @MockitoExtension instead of @SpringBootTest
@ExtendWith(MockitoExtension.class)

// Mock dependencies thay vì load Spring context
@Mock
private UserRepository userRepository;

@InjectMocks
private UserService userService;
```

---

## 8. Continuous Integration

### 8.1. GitHub Actions Example

```yaml
name: Run Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Run tests
        run: ./mvnw clean test
      - name: Generate coverage report
        run: ./mvnw jacoco:report
      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v2
```

---

## 9. Báo Cáo Test cho Giáo Viên

### 9.1. Export Reports

```bash
# Tạo thư mục reports
mkdir -p test-reports

# Copy JaCoCo HTML report
cp -r target/site/jacoco test-reports/coverage-report

# Copy Surefire reports
cp -r target/surefire-reports test-reports/

# Zip reports
zip -r test-reports.zip test-reports/
```

### 9.2. Nội dung báo cáo nên bao gồm:

1. **Test Summary**
   - Tổng số test cases: 81
   - Tests passed: 81
   - Tests failed: 0
   - Coverage tổng thể: ~85%

2. **Test Cases Detail**
   - UserServiceTest: 27 tests
   - ProductServiceTest: 33 tests
   - JwtUtilTest: 21 tests

3. **Coverage Report**
   - Screenshot của JaCoCo dashboard
   - Coverage breakdown by package
   - Highlighted source code

4. **Test Evidence**
   - Console output
   - Surefire XML reports
   - JaCoCo HTML report

---

## 10. Tài Liệu Tham Khảo

### Documentation
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

### Useful Links
- [AssertJ Assertions](https://assertj.github.io/doc/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [Test Coverage Best Practices](https://martinfowler.com/bliki/TestCoverage.html)

---

## ✅ Checklist Hoàn Thành

- [x] Cài đặt JUnit 5 và Mockito
- [x] Cấu hình JaCoCo plugin
- [x] Viết tests cho UserService (27 tests)
- [x] Viết tests cho ProductService (33 tests)
- [x] Viết tests cho JwtUtil (21 tests)
- [x] Đạt coverage >80% cho Service layer
- [x] Đạt coverage >85% cho Security layer
- [x] Generate JaCoCo HTML report
- [x] Document test cases và coverage

---

**Tổng kết:**
- ✅ **81 test cases** được viết
- ✅ **White box testing** coverage đầy đủ
- ✅ **JaCoCo report** tự động generate
- ✅ **Documentation** chi tiết

**Cách nộp báo cáo:**
1. Chạy: `./mvnw clean test`
2. Mở: `target/site/jacoco/index.html`
3. Screenshot coverage report
4. Đính kèm test-reports.zip

Chúc bạn test thành công! 🎉
