package com.example.demo.services;

import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.ExpenseResponseDTO;
import com.example.demo.dto.IncomeRequestDTO;
import com.example.demo.dto.IncomeResponseDTO;
import com.example.demo.model.Income;
import com.example.demo.model.User;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.IncomeRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public IncomeResponseDTO addIncome(String email, IncomeRequestDTO dto){
        User user = userRepository.findByEmail(email).orElseThrow();

        Income income = new Income();
        income.setSource(dto.getSource());
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setComments(dto.getComments());
        income.setUser(user);

        Income saved = incomeRepository.save(income);

        return new IncomeResponseDTO(
                saved.getId(),
                saved.getSource(),
                saved.getAmount(),
                saved.getDate(),
                saved.getComments()
                );
    }

    public List<IncomeResponseDTO> getIncome(String email){
        User user = userRepository.findByEmail(email).orElseThrow();

        return incomeRepository.findByUser(user).stream().map(e-> new IncomeResponseDTO(

                e.getId(),
                e.getSource(),
                e.getAmount(),
                e.getDate(),
                e.getComments()
        )).toList();
    }


    public IncomeResponseDTO updateIncome(
            Long id, String email, IncomeRequestDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        income.setSource(dto.getSource());
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setComments(dto.getComments());

        Income updated = incomeRepository.save(income);
        return mapToResponse(updated);
    }


    public void deleteIncome(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!income.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        incomeRepository.delete(income);
    }


    private IncomeResponseDTO mapToResponse(Income income) {
        return new IncomeResponseDTO(
                income.getId(),
                income.getSource(),
                income.getAmount(),
                income.getDate(),
                income.getComments()
        );
    }
}
