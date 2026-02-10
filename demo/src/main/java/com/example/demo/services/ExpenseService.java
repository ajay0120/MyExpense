package com.example.demo.services;

import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.ExpenseResponseDTO;
import com.example.demo.model.Expense;
import com.example.demo.model.User;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public ExpenseResponseDTO addExpense(String email, ExpenseRequestDTO dto){
        User user = userRepository.findByEmail(email).orElseThrow();

        Expense expense = new Expense();
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setComments(dto.getComments());
        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);

        return new ExpenseResponseDTO(
                saved.getId(),
                saved.getTitle(),
                saved.getAmount(),
                saved.getDate(),
                saved.getComments()
        );
    }

    public List<ExpenseResponseDTO> getExpenses(String email){
        User user = userRepository.findByEmail(email).orElseThrow();

        return expenseRepository.findByUser(user).stream().map(e-> new ExpenseResponseDTO(
                e.getId(),
                e.getTitle(),
                e.getAmount(),
                e.getDate(),
                e.getComments()
        )).toList();
    }
    private ExpenseResponseDTO mapToResponse(Expense expense){
        return  new ExpenseResponseDTO(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getDate(),
                expense.getComments()
        );
    }
    public ExpenseResponseDTO updateExpense(
            Long id, String email, ExpenseRequestDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setComments(dto.getComments());

        Expense updated = expenseRepository.save(expense);

        return mapToResponse(updated);
    }

    public void deleteExpense(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        expenseRepository.delete(expense);
    }
}
