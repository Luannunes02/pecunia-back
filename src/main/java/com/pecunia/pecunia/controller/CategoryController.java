package com.pecunia.pecunia.controller;

import com.pecunia.pecunia.dto.request.CategoryRequest;
import com.pecunia.pecunia.entity.Category;
import com.pecunia.pecunia.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
      @AuthenticationPrincipal UserDetails userDetails) {
    Category category = categoryService.createCategory(request, userDetails.getUsername());
    return ResponseEntity.ok(category);
  }

  @GetMapping
  @Operation(summary = "Listar todas as categorias ativas do usuário")
  public ResponseEntity<List<Category>> getCategories(@AuthenticationPrincipal UserDetails userDetails) {
    List<Category> categories = categoryService.getCategories(userDetails.getUsername());
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/type/{type}")
  @Operation(summary = "Listar categorias por tipo")
  public ResponseEntity<List<Category>> getCategoriesByType(
      @PathVariable String type,
      @AuthenticationPrincipal UserDetails userDetails) {
    List<Category> categories = categoryService.getCategoriesByType(userDetails.getUsername(), type);
    return ResponseEntity.ok(categories);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualizar uma categoria")
  public ResponseEntity<Category> updateCategory(
      @PathVariable Long id,
      @Valid @RequestBody CategoryRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    Category category = categoryService.updateCategory(id, request, userDetails.getUsername());
    return ResponseEntity.ok(category);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Excluir uma categoria")
  public ResponseEntity<Void> deleteCategory(
      @PathVariable Long id,
      @AuthenticationPrincipal UserDetails userDetails) {
    categoryService.deleteCategory(id, userDetails.getUsername());
    return ResponseEntity.noContent().build();
  }
}
