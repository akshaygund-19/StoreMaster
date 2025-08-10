package com.akshay.StoreMaster.controller;

import com.akshay.StoreMaster.dto.UserLoginDTO;
import com.akshay.StoreMaster.dto.UserRegistrationDTO;
import com.akshay.StoreMaster.repository.UserRepository;
import com.akshay.StoreMaster.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storeMaster/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/hello")
    public String inService(){
        return "Hello from StoreMaster Service";
    }

    @PostMapping("/register")
    public String addUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) throws Exception{
        var user = userService.registerUser(userRegistrationDTO);
        return "Registration Successfully Completed for User: {} " + userRegistrationDTO.getName();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) throws Exception{
        userService.login(userLoginDTO);
        return ResponseEntity.ok("Logged in Successfully");
    }
}
