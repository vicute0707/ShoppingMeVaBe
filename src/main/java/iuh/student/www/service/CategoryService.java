package iuh.student.www.service;

import iuh.student.www.entity.Category;
import iuh.student.www.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) throws Exception {
        if (categoryRepository.existsByName(category.getName())) {
            throw new Exception("Category name already exists");
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories() {
        return categoryRepository.findByActiveTrue();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Long id, Category updatedCategory) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));

        // Check if new name already exists (excluding current category)
        Optional<Category> existing = categoryRepository.findByName(updatedCategory.getName());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new Exception("Category name already exists");
        }

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        category.setActive(updatedCategory.getActive());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) throws Exception {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));

        // Check if category has products
        long productCount = categoryRepository.countProductsByCategoryId(id);
        if (productCount > 0) {
            throw new Exception("Cannot delete category with existing products. Please delete or reassign all products first.");
        }

        categoryRepository.delete(category);
    }

    public long countProductsInCategory(Long categoryId) {
        return categoryRepository.countProductsByCategoryId(categoryId);
    }
}
