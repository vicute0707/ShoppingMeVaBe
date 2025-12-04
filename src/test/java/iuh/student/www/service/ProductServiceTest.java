package iuh.student.www.service;

import iuh.student.www.entity.Category;
import iuh.student.www.entity.Product;
import iuh.student.www.repository.CategoryRepository;
import iuh.student.www.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * White Box Testing cho ProductService
 * Test coverage: Tất cả các đường dẫn logic trong ProductService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService White Box Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Test Category")
                .description("Test Description")
                .active(true)
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Test Product")
                .description("Test Description")
                .price(100000.0)
                .stockQuantity(50)
                .imageUrl("http://test.com/image.jpg")
                .active(true)
                .category(testCategory)
                .build();
    }

    // ==================== CREATE PRODUCT TESTS ====================

    @Test
    @DisplayName("Test 1: Create product thành công - Happy path")
    void testCreateProduct_Success() throws Exception {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.createProduct(testProduct);

        // Then
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(100000.0, result.getPrice());
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 2: Create product - Category null (Branch 1)")
    void testCreateProduct_CategoryNull() {
        // Given
        testProduct.setCategory(null);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Vui lòng chọn danh mục cho sản phẩm", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 3: Create product - Category ID null (Branch 1)")
    void testCreateProduct_CategoryIdNull() {
        // Given
        testProduct.getCategory().setId(null);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Vui lòng chọn danh mục cho sản phẩm", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 4: Create product - Category không tồn tại (Branch 2)")
    void testCreateProduct_CategoryNotFound() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Không tìm thấy danh mục đã chọn", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 5: Create product - Name null (Branch 3)")
    void testCreateProduct_NameNull() {
        // Given
        testProduct.setName(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Tên sản phẩm không được để trống", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 6: Create product - Name empty (Branch 3)")
    void testCreateProduct_NameEmpty() {
        // Given
        testProduct.setName("   ");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Tên sản phẩm không được để trống", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 7: Create product - Price null (Branch 4)")
    void testCreateProduct_PriceNull() {
        // Given
        testProduct.setPrice(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Giá sản phẩm phải lớn hơn 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 8: Create product - Price zero (Branch 4)")
    void testCreateProduct_PriceZero() {
        // Given
        testProduct.setPrice(0.0);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Giá sản phẩm phải lớn hơn 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 9: Create product - Price negative (Branch 4)")
    void testCreateProduct_PriceNegative() {
        // Given
        testProduct.setPrice(-100.0);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Giá sản phẩm phải lớn hơn 0", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 10: Create product - Stock null (Branch 5)")
    void testCreateProduct_StockNull() {
        // Given
        testProduct.setStockQuantity(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Số lượng kho không được âm", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 11: Create product - Stock negative (Branch 5)")
    void testCreateProduct_StockNegative() {
        // Given
        testProduct.setStockQuantity(-10);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.createProduct(testProduct);
        });

        assertEquals("Số lượng kho không được âm", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    // ==================== UPDATE PRODUCT TESTS ====================

    @Test
    @DisplayName("Test 12: Update product thành công - Happy path")
    void testUpdateProduct_Success() throws Exception {
        // Given
        Product updatedProduct = Product.builder()
                .name("Updated Product")
                .description("Updated Description")
                .price(150000.0)
                .stockQuantity(100)
                .category(testCategory)
                .active(true)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.updateProduct(1L, updatedProduct);

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 13: Update product - Product không tồn tại")
    void testUpdateProduct_ProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.updateProduct(1L, testProduct);
        });

        assertEquals("Không tìm thấy sản phẩm", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 14: Update product - Active null defaults to true")
    void testUpdateProduct_ActiveNull() throws Exception {
        // Given
        Product updatedProduct = Product.builder()
                .name("Updated Product")
                .price(150000.0)
                .stockQuantity(100)
                .category(testCategory)
                .active(null)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        Product result = productService.updateProduct(1L, updatedProduct);

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ==================== DELETE PRODUCT TESTS ====================

    @Test
    @DisplayName("Test 15: Delete product - Product không tồn tại")
    void testDeleteProduct_ProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Không tìm thấy sản phẩm", exception.getMessage());
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    @DisplayName("Test 16: Delete product - Product có trong orders (Branch 1)")
    void testDeleteProduct_ProductInOrders() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.isProductInOrders(1L)).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.deleteProduct(1L);
        });

        assertTrue(exception.getMessage().contains("Không thể xóa sản phẩm đã có trong đơn hàng"));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    @DisplayName("Test 17: Delete product thành công - Happy path")
    void testDeleteProduct_Success() throws Exception {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.isProductInOrders(1L)).thenReturn(false);
        doNothing().when(productRepository).delete(any(Product.class));

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).isProductInOrders(1L);
        verify(productRepository, times(1)).delete(testProduct);
    }

    // ==================== UPDATE STOCK TESTS ====================

    @Test
    @DisplayName("Test 18: Update stock - Product không tồn tại")
    void testUpdateStock_ProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.updateStock(1L, 10);
        });

        assertEquals("Không tìm thấy sản phẩm", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 19: Update stock - Không đủ hàng trong kho (Branch 1)")
    void testUpdateStock_InsufficientStock() {
        // Given
        testProduct.setStockQuantity(10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            productService.updateStock(1L, 20);
        });

        assertTrue(exception.getMessage().contains("Không đủ số lượng trong kho"));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 20: Update stock thành công - Happy path")
    void testUpdateStock_Success() throws Exception {
        // Given
        testProduct.setStockQuantity(50);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        productService.updateStock(1L, 10);

        // Then
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Test 21: Update stock - Exact stock quantity")
    void testUpdateStock_ExactStock() throws Exception {
        // Given
        testProduct.setStockQuantity(10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        productService.updateStock(1L, 10);

        // Then
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ==================== SEARCH & QUERY TESTS ====================

    @Test
    @DisplayName("Test 22: Get all products")
    void testGetAllProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test 23: Get active products")
    void testGetActiveProducts() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByActiveTrue()).thenReturn(products);

        // When
        List<Product> result = productService.getActiveProducts();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByActiveTrue();
    }

    @Test
    @DisplayName("Test 24: Get products by category")
    void testGetProductsByCategory() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByCategoryIdAndActiveTrue(1L)).thenReturn(products);

        // When
        List<Product> result = productService.getProductsByCategory(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByCategoryIdAndActiveTrue(1L);
    }

    @Test
    @DisplayName("Test 25: Search products - With keyword")
    void testSearchProducts_WithKeyword() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.searchActiveProducts("test")).thenReturn(products);

        // When
        List<Product> result = productService.searchProducts("test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).searchActiveProducts("test");
    }

    @Test
    @DisplayName("Test 26: Search products - Null keyword (Branch 1)")
    void testSearchProducts_NullKeyword() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByActiveTrue()).thenReturn(products);

        // When
        List<Product> result = productService.searchProducts(null);

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).findByActiveTrue();
        verify(productRepository, never()).searchActiveProducts(anyString());
    }

    @Test
    @DisplayName("Test 27: Search products - Empty keyword (Branch 1)")
    void testSearchProducts_EmptyKeyword() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByActiveTrue()).thenReturn(products);

        // When
        List<Product> result = productService.searchProducts("   ");

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).findByActiveTrue();
        verify(productRepository, never()).searchActiveProducts(anyString());
    }

    @Test
    @DisplayName("Test 28: Search all products - With keyword")
    void testSearchAllProducts_WithKeyword() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.searchAllProducts("test")).thenReturn(products);

        // When
        List<Product> result = productService.searchAllProducts("test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).searchAllProducts("test");
    }

    @Test
    @DisplayName("Test 29: Search all products - Null keyword")
    void testSearchAllProducts_NullKeyword() {
        // Given
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productService.searchAllProducts(null);

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).findAll();
        verify(productRepository, never()).searchAllProducts(anyString());
    }

    @Test
    @DisplayName("Test 30: Get product by ID - Found")
    void testGetProductById_Found() {
        // Given
        when(productRepository.findByIdWithCategory(1L)).thenReturn(Optional.of(testProduct));

        // When
        Optional<Product> result = productService.getProductById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(productRepository, times(1)).findByIdWithCategory(1L);
    }

    @Test
    @DisplayName("Test 31: Get product by ID - Not found")
    void testGetProductById_NotFound() {
        // Given
        when(productRepository.findByIdWithCategory(999L)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.getProductById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findByIdWithCategory(999L);
    }

    @Test
    @DisplayName("Test 32: Is product in orders - True")
    void testIsProductInOrders_True() {
        // Given
        when(productRepository.isProductInOrders(1L)).thenReturn(true);

        // When
        boolean result = productService.isProductInOrders(1L);

        // Then
        assertTrue(result);
        verify(productRepository, times(1)).isProductInOrders(1L);
    }

    @Test
    @DisplayName("Test 33: Is product in orders - False")
    void testIsProductInOrders_False() {
        // Given
        when(productRepository.isProductInOrders(1L)).thenReturn(false);

        // When
        boolean result = productService.isProductInOrders(1L);

        // Then
        assertFalse(result);
        verify(productRepository, times(1)).isProductInOrders(1L);
    }
}
