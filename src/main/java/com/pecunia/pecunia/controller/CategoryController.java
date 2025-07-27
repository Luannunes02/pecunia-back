package com.pecunia.pecunia.controller;

import com.pecunia.pecunia.dto.request.CategoryRequest;
import com.pecunia.pecunia.entity.Category;
import com.pecunia.pecunia.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias")
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  @Operation(summary = "Criar uma nova categoria")
  public ResponseEntity<Category> createCategory(
      @Valid @RequestBody CategoryRequest request,
      Authentication authentication) {
    Category category = categoryService.createCategory(request, authentication.getName());
    return ResponseEntity.ok(category);
  }

  @GetMapping
  @Operation(summary = "Listar todas as categorias do usuário")
  public ResponseEntity<List<Category>> getCategories(Authentication authentication) {
    List<Category> categories = categoryService.getCategories(authentication.getName());
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/type/{type}")
  @Operation(summary = "Listar categorias por tipo")
  public ResponseEntity<List<Category>> getCategoriesByType(
      @PathVariable String type,
      Authentication authentication) {
    List<Category> categories = categoryService.getCategoriesByType(authentication.getName(), type);
    return ResponseEntity.ok(categories);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar uma categoria")
  public ResponseEntity<Category> updateCategory(
      @PathVariable Long id,
      @Valid @RequestBody CategoryRequest request,
      Authentication authentication) {
    Category category = categoryService.updateCategory(id, request, authentication.getName());
    return ResponseEntity.ok(category);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir uma categoria")
  public ResponseEntity<Void> deleteCategory(
      @PathVariable Long id,
      Authentication authentication) {
    categoryService.deleteCategory(id, authentication.getName());
    return ResponseEntity.noContent().build();
  }
}