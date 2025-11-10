package iuh.student.www.service;

import iuh.student.www.entity.Category;
import iuh.student.www.entity.Product;
import iuh.student.www.repository.CategoryRepository;
import iuh.student.www.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product createProduct(Product product) throws Exception {
        // Validate category exists
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new Exception("Vui lòng chọn danh mục cho sản phẩm");
        }

        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new Exception("Không tìm thấy danh mục đã chọn"));

        product.setCategory(category);

        // Validate name
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new Exception("Tên sản phẩm không được để trống");
        }

        // Validate price
        if (product.getPrice() == null || product.getPrice().doubleValue() <= 0) {
            throw new Exception("Giá sản phẩm phải lớn hơn 0");
        }

        // Validate stock
        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new Exception("Số lượng kho không được âm");
        }

        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndActiveTrue(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getActiveProducts();
        }
        return productRepository.searchActiveProducts(keyword);
    }

    public List<Product> searchAllProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.searchAllProducts(keyword);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdWithCategory(id);
    }

    public Product updateProduct(Long id, Product updatedProduct) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm"));

        // Validate and update category
        if (updatedProduct.getCategory() == null || updatedProduct.getCategory().getId() == null) {
            throw new Exception("Vui lòng chọn danh mục cho sản phẩm");
        }

        Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
                .orElseThrow(() -> new Exception("Không tìm thấy danh mục đã chọn"));
        product.setCategory(category);

        // Validate name
        if (updatedProduct.getName() == null || updatedProduct.getName().trim().isEmpty()) {
            throw new Exception("Tên sản phẩm không được để trống");
        }
        product.setName(updatedProduct.getName());

        // Validate and update price
        if (updatedProduct.getPrice() == null || updatedProduct.getPrice().doubleValue() <= 0) {
            throw new Exception("Giá sản phẩm phải lớn hơn 0");
        }
        product.setPrice(updatedProduct.getPrice());

        // Validate and update stock
        if (updatedProduct.getStockQuantity() == null || updatedProduct.getStockQuantity() < 0) {
            throw new Exception("Số lượng kho không được âm");
        }
        product.setStockQuantity(updatedProduct.getStockQuantity());

        // Update other fields
        product.setDescription(updatedProduct.getDescription());
        product.setImageUrl(updatedProduct.getImageUrl());
        product.setActive(updatedProduct.getActive() != null ? updatedProduct.getActive() : true);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm"));

        // Check if product is in any orders
        if (productRepository.isProductInOrders(id)) {
            throw new Exception("Không thể xóa sản phẩm đã có trong đơn hàng. Bạn có thể ẩn sản phẩm thay vì xóa.");
        }

        productRepository.delete(product);
    }

    public boolean isProductInOrders(Long productId) {
        return productRepository.isProductInOrders(productId);
    }

    public void updateStock(Long productId, Integer quantity) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Không tìm thấy sản phẩm"));

        int newStock = product.getStockQuantity() - quantity;
        if (newStock < 0) {
            throw new Exception("Không đủ số lượng trong kho cho sản phẩm: " + product.getName());
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);
    }
}
