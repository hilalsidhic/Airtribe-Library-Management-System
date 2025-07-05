package com.hilal.newsApp.service;

import com.hilal.newsApp.entity.User;
import com.hilal.newsApp.entity.dto.*;

import java.util.List;

public interface UserService {
    UserDTO registerUser(RegisterDTO registerDTO);
    LoginResponseDTO loginUser(LoginDTO loginDTO);
    String logoutUser(String username);
    UserDTO updateEmail(UpdateEmailDTO updateEmailDTO);
    String updatePassword(UpdatePasswordDTO updatePasswordDTO);
    String deleteUser(String username);
    UserDTO getUserDetails(String username);
    UserDTO getUserDetailsById(Long userId);
    User getUserEntityById(Long userId);
    List<UserDTO> getAllUsers();
}