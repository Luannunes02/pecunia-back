package com.pecunia.pecunia.repository;

import com.pecunia.pecunia.entity.Category;
import com.pecunia.pecunia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByUser(User user);

  List<Category> findByUserAndType(User user, String type);

  List<Category> findByUserAndIsActiveTrue(User user);
}