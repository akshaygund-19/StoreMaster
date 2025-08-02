package com.akshay.StoreMaster.service;

import com.akshay.StoreMaster.dto.UserLoginDTO;
import com.akshay.StoreMaster.dto.UserRegistrationDTO;
import com.akshay.StoreMaster.dto.UserResponseDTO;
import com.akshay.StoreMaster.entity.User;
import com.akshay.StoreMaster.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        User existingUser = userRepository.findByEmail(userRegistrationDTO.getEmail());
        if (existingUser != null) {
            throw new IllegalArgumentException("Email already registered");
        } else {
            String encryptedPassword = bCryptPasswordEncoder.encode(userRegistrationDTO.getPassword());
            user.setPassword(encryptedPassword);
            user.setName(userRegistrationDTO.getName());
            user.setEmail(userRegistrationDTO.getEmail());
            user.setRole("ROLE_USER");
            user.setCreated_at(LocalDateTime.now());
            User savedUser = userRepository.save(user);

            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(savedUser.getId());
            responseDTO.setName(savedUser.getName());
            responseDTO.setRole(savedUser.getRole());
            responseDTO.setEmail(savedUser.getEmail());
            return responseDTO;
        }
    }
    public void login(UserLoginDTO userLoginDTO){
        User checkUser = userRepository.findByEmail(userLoginDTO.getEmail());
        if (checkUser == null){
            throw new IllegalArgumentException("Invalid Credential Email is not valid ");
        }
        if (!bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), checkUser.getPassword())) {
            throw new IllegalArgumentException("Invalid Credential: Password Mismatch");
        }
    }

}
