package com.example.demo.services;

import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(AuthRequestDTO dto){
        if(userRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("User Already exists");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

    }
    public String login(AuthRequestDTO dto){
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(()->new RuntimeException("Invalid Credentials"));

        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid Credentials");

        }
        return jwtUtil.generateToken(user.getEmail());
    }
}
