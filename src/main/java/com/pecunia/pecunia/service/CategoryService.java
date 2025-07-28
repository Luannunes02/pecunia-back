package com.pecunia.pecunia.service;

import com.pecunia.pecunia.dto.request.CategoryRequest;
import com.pecunia.pecunia.entity.Category;
import com.pecunia.pecunia.entity.User;
import com.pecunia.pecunia.repository.CategoryRepository;
import com.pecunia.pecunia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  @Transactional
  public Category createCategory(CategoryRequest request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Category category = new Category();
    category.setName(request.getName());
    category.setType(request.getType());
    category.setIconName(request.getIconName());
    category.setColorHex(request.getColorHex());
    category.setIsActive(true);
    category.setUser(user);

    return categoryRepository.save(category);
  }

  public List<Category> getCategories(String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    return categoryRepository.findByUserAndIsActiveTrue(user);
  }

  public List<Category> getCategoriesByType(String userEmail, String type) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    return categoryRepository.findByUserAndType(user, type);
  }

  @Transactional
  public Category updateCategory(Long id, CategoryRequest request, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

    if (!category.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Categoria não pertence ao usuário");
    }

    category.setName(request.getName());
    category.setType(request.getType());
    category.setIconName(request.getIconName());
    category.setColorHex(request.getColorHex());

    return categoryRepository.save(category);
  }

  @Transactional
  public void deleteCategory(Long id, String userEmail) {
    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

    if (!category.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Categoria não pertence ao usuário");
    }

    category.setIsActive(false);
    categoryRepository.save(category);
  }
}