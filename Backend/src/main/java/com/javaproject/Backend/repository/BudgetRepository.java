package com.javaproject.Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaproject.Backend.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

}