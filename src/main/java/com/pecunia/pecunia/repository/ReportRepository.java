package com.pecunia.pecunia.repository;

import com.pecunia.pecunia.entity.Report;
import com.pecunia.pecunia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
  List<Report> findByUser(User user);
}