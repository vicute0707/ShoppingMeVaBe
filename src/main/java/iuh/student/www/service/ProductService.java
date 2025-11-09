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
            throw new Exception("Category is required");
        }

        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new Exception("Category not found"));

        product.setCategory(category);
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
                .orElseThrow(() -> new Exception("Product not found"));

        // Validate category
        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
                    .orElseThrow(() -> new Exception("Category not found"));
            product.setCategory(category);
        }

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStockQuantity(updatedProduct.getStockQuantity());
        product.setImageUrl(updatedProduct.getImageUrl());
        product.setActive(updatedProduct.getActive());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) throws Exception {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));

        // Check if product is in any orders
        if (productRepository.isProductInOrders(id)) {
            throw new Exception("Cannot delete product that has been ordered. You can deactivate it instead.");
        }

        productRepository.delete(product);
    }

    public boolean isProductInOrders(Long productId) {
        return productRepository.isProductInOrders(productId);
    }

    public void updateStock(Long productId, Integer quantity) throws Exception {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found"));

        int newStock = product.getStockQuantity() - quantity;
        if (newStock < 0) {
            throw new Exception("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);
    }
}
