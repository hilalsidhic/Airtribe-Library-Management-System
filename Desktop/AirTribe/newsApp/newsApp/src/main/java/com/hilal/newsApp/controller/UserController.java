package com.hilal.newsApp.controller;

import com.hilal.newsApp.entity.dto.*;
import com.hilal.newsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/auth/register")
    public String register1User() {
        return "userService.registerUser(registerDTO)";
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody RegisterDTO registerDTO) {
        return userService.registerUser(registerDTO);
    }
    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO loginDTO) {
        return userService.loginUser(loginDTO);
    }
    @PostMapping("/logout")
    public String logoutUser(@RequestBody LogoutDTO logoutDTO) {
        return userService.logoutUser(logoutDTO.getUsername());
    }
    @PutMapping("/update-email")
    public UserDTO updateEmail(@RequestBody UpdateEmailDTO updateEmail) {
        return userService.updateEmail(updateEmail);
    }
    @PutMapping("/update-password")
    public String updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return userService.updatePassword(updatePasswordDTO);
    }
    @DeleteMapping("/delete-user/{username}")
    public String deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }
    @GetMapping("/user/{username}")
    public UserDTO getUserDetails(@PathVariable String username) {
        return userService.getUserDetails(username);
    }
    @GetMapping("/user")
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

}
