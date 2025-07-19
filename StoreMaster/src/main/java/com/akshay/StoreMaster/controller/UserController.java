package com.akshay.StoreMaster.controller;

import com.akshay.StoreMaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/storeMaster")
public class UserController {


    @Autowired
    private UserService userService;

}
