package com.example.demo.controller;

import com.example.demo.dto.ExpenseRequestDTO;
import com.example.demo.dto.ExpenseResponseDTO;
import com.example.demo.dto.IncomeRequestDTO;
import com.example.demo.dto.IncomeResponseDTO;
import com.example.demo.services.IncomeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@CrossOrigin
public class IncomeController {
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public IncomeResponseDTO addIncome(Authentication authentication, @RequestBody IncomeRequestDTO dto){
        String email = authentication.getName();
        return incomeService.addIncome(email, dto);
    }

    @GetMapping
    public List<IncomeResponseDTO> getIncome(Authentication authentication){
        String email = authentication.getName();
        return incomeService.getIncome(email);

    }

    @PutMapping("/{id}")
    public IncomeResponseDTO updateIncome(
            @PathVariable Long id,
            Authentication authentication,
            @RequestBody IncomeRequestDTO dto
    ){
        String email = authentication.getName();
        return incomeService.updateIncome(id,email,dto);
    }

    @DeleteMapping("/{id}")
    public void deleteIncome(
            @PathVariable Long id,
            Authentication authentication
    ){
        String email = authentication.getName();
        incomeService.deleteIncome(id,email);

    }


}
