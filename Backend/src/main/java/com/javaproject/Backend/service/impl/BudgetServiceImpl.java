package com.javaproject.Backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.javaproject.Backend.domain.Budget;
import com.javaproject.Backend.domain.Category;
import com.javaproject.Backend.domain.User;
import com.javaproject.Backend.dto.request.BudgetRequest;
import com.javaproject.Backend.dto.request.update.BudgetUpdateRequest;
import com.javaproject.Backend.dto.response.BudgetResponse;
import com.javaproject.Backend.exception.ResourceNotFoundException;
import com.javaproject.Backend.repository.BudgetRepository;
import com.javaproject.Backend.repository.CategoryRepository;
import com.javaproject.Backend.repository.UserRepository;
import com.javaproject.Backend.service.BudgetService;
import com.javaproject.Backend.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {
    
}
