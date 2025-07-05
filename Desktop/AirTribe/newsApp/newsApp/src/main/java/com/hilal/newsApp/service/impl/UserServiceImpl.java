package com.hilal.newsApp.service.impl;

import com.hilal.newsApp.entity.User;
import com.hilal.newsApp.entity.dto.*;
import com.hilal.newsApp.exception.DuplicateResourceException;
import com.hilal.newsApp.exception.InvalidCredentialsException;
import com.hilal.newsApp.exception.ResourceNotFoundException;
import com.hilal.newsApp.repository.UserRepository;
import com.hilal.newsApp.security.JwtUtils;
import com.hilal.newsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private PasswordEncoder _passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDTO registerUser(RegisterDTO registerDTO) {
        if(_userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new DuplicateResourceException("User already exists");
        }
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(_passwordEncoder.encode(registerDTO.getPassword()))
                .email(registerDTO.getEmail())
                .isActive(true)
                .role(Set.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();
        User response = _userRepository.save(user);
        return UserDTO.builder()
                .username(response.getUsername())
                .email(response.getEmail())
                .build();
    }

    @Override
    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        Optional<User> user = _userRepository.findByUsername(loginDTO.getUsername());
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User does not exist");
        }
        if(!_passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        user.get().setLastLogin(LocalDateTime.now());
        _userRepository.save(user.get());
        String token = jwtUtils.generateJwtToken(user.get().getUsername());
        return LoginResponseDTO.builder()
                .username(user.get().getUsername())
                .token(token)
                .build();
    }

    @Override
    public String logoutUser(String username) {
        Optional<User> user = _userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User does not exist");
        }
        return "User " + username + " logged out successfully";
    }

    @Override
    public UserDTO updateEmail(UpdateEmailDTO updateEmailDTO) {
        String username = updateEmailDTO.getUsername();
        String newEmail = updateEmailDTO.getNewEmail();
        Optional<User> user = _userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User does not exist");
        }
        user.get().setEmail(newEmail);
        User updatedUser = _userRepository.save(user.get());
        System.out.println(updatedUser);
        return UserDTO.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .build();
    }

    @Override
    public String updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        String username = updatePasswordDTO.getUsername();
        String oldPassword = updatePasswordDTO.getOldPassword();
        String newPassword = updatePasswordDTO.getNewPassword();
        Optional<User> user = _userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User does not exist");
        }
        if(!user.get().getPassword().equals(oldPassword)) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }
        user.get().setPassword(newPassword);
        User updatedUser = _userRepository.save(user.get());
        return "Password updated successfully";
    }

    @Override
    public String deleteUser(String username) {
        Optional<User> user = _userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User does not exist");
        }
        _userRepository.delete(user.get());
        return "User " + username + " deleted successfully";
    }

    @Override
    public UserDTO getUserDetails(String username) {
        Optional<User> user = _userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new ResourceNotFoundException("User does not exist");
        }
        User foundUser = user.get();
        return UserDTO.builder()
                .id(foundUser.getId())
                .username(foundUser.getUsername())
                .email(foundUser.getEmail())
                .build();
    }

    @Override
    public UserDTO getUserDetailsById(Long userId) {
        Optional<User> user = _userRepository.findById(userId);
        if (user.isEmpty()){
            throw new ResourceNotFoundException("User does not exist");
        }
        User foundUser = user.get();
        return UserDTO.builder()
                .id(foundUser.getId())
                .username(foundUser.getUsername())
                .email(foundUser.getEmail())
                .build();
    }

    @Override
    public User getUserEntityById(Long userId) {
        Optional<User> user = _userRepository.findById(userId);
        if (user.isEmpty()){
            throw new ResourceNotFoundException("User does not exist");
        }
        return user.get();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = _userRepository.findAll();
        return users.stream().map(user -> UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build())
                .toList();
    }
}
