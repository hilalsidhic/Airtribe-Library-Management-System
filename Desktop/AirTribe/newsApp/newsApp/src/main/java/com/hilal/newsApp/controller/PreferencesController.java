package com.hilal.newsApp.controller;

import com.hilal.newsApp.entity.dto.NewsPreferencesDTO;
import com.hilal.newsApp.service.PreferenceService;
import com.hilal.newsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PreferencesController {

    @Autowired
    private UserService userService;
    @Autowired
    private PreferenceService _preferenceService;

    @GetMapping("/preferences")
    public NewsPreferencesDTO getUserPreferences() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.getUserDetails(username).getId();
         return _preferenceService.getPreferences(userId);
    }

    @PostMapping("/preferences/{userId}")
    public NewsPreferencesDTO saveUserPreferences(@PathVariable Long userId, @RequestBody NewsPreferencesDTO preferencesDTO) {
        return _preferenceService.savePreferences(userId, preferencesDTO);
    }

    @PutMapping("/preferences")
    public NewsPreferencesDTO updateUserPreferences(@RequestBody NewsPreferencesDTO preferencesDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.getUserDetails(username).getId();
        return _preferenceService.updatePreferences(userId, preferencesDTO);
    }

    @PutMapping("/preferences/{userId}/language/{language}")
    public NewsPreferencesDTO updateUserLanguage(@PathVariable Long userId, @PathVariable String language) {
        return _preferenceService.setLanguage(userId, language);
    }

    @DeleteMapping("/preferences/{userId}")
    public String deleteUserPreferences(@PathVariable Long userId) {
         return _preferenceService.deletePreferences(userId);
    }
}
