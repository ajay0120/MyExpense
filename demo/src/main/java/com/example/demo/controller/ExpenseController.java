package com.example.demo.controller;

import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.ExpenseResponseDTO;
import com.example.demo.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")

@CrossOrigin
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponseDTO addExpense(
            Authentication authentication,
            @RequestBody ExpenseRequestDTO dto){
        String email = authentication.getName();
        return expenseService.addExpense(email, dto);
    }

    @GetMapping
    public List<ExpenseResponseDTO> getExpenses(
            Authentication authentication
    ){
        String email = authentication.getName();
        return expenseService.getExpenses(email);
    }
    @PutMapping("/{id}")
    public ExpenseResponseDTO updateExpense(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody ExpenseRequestDTO dto
    ){
        String email = authentication.getName();
        return expenseService.updateExpense(id,email,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(
            @PathVariable Long id,
            Authentication authentication
    ){
        String email = authentication.getName();
        expenseService.deleteExpense(id,email);

    }

}
