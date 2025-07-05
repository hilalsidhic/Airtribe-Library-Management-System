package com.hilal.newsApp.service.impl;

import com.hilal.newsApp.entity.NewsPreferences;
import com.hilal.newsApp.entity.User;
import com.hilal.newsApp.entity.dto.NewsPreferencesDTO;
import com.hilal.newsApp.exception.DuplicateResourceException;
import com.hilal.newsApp.exception.ResourceNotFoundException;
import com.hilal.newsApp.repository.PreferencesRepository;
import com.hilal.newsApp.repository.UserRepository;
import com.hilal.newsApp.service.PreferenceService;
import com.hilal.newsApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

    @Autowired
    private PreferencesRepository _preferencesRepository;
    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private UserService _userService;

    @Override
    public NewsPreferencesDTO savePreferences(Long userId, NewsPreferencesDTO preferencesDTO) {
        User user = _userService.getUserEntityById(userId);
        NewsPreferences existingPreferences = user.getNewsPreferences();
        if (existingPreferences != null) {
            throw new DuplicateResourceException("Preferences already exist for this user");
        }
        NewsPreferences newsPreferences = _preferencesRepository.save(
                NewsPreferences.builder()
                        .user(user)
                        .preferredLanguage(preferencesDTO.getPreferredLanguage())
                        .preferredCategories(preferencesDTO.getPreferredCategories())
                        .preferredSources(preferencesDTO.getPreferredSources())
                        .preferredCountries(preferencesDTO.getPreferredCountries())
                        .build()
        );
        user.setNewsPreferences(newsPreferences);
        _userRepository.save(user);
        return NewsPreferencesDTO.builder()
                .userId(newsPreferences.getUser().getId())
                .username(newsPreferences.getUser().getUsername())
                .preferredLanguage(newsPreferences.getPreferredLanguage())
                .preferredCategories(newsPreferences.getPreferredCategories())
                .preferredSources(newsPreferences.getPreferredSources())
                .preferredCountries(newsPreferences.getPreferredCountries())
                .build();
    }

    @Override
    public NewsPreferencesDTO updatePreferences(Long userId, NewsPreferencesDTO preferencesDTO) {
        User user = _userService.getUserEntityById(userId);
        NewsPreferences existingPreferences = user.getNewsPreferences();
        if (existingPreferences == null) {
            return savePreferences(userId, preferencesDTO);
        }
        if(preferencesDTO.getPreferredLanguage()!= null) {
            existingPreferences.setPreferredLanguage(preferencesDTO.getPreferredLanguage());
        }
        if(preferencesDTO.getPreferredCategories() != null) {
            existingPreferences.setPreferredCategories(preferencesDTO.getPreferredCategories());
        }
        if(preferencesDTO.getPreferredSources() != null) {
            existingPreferences.setPreferredSources(preferencesDTO.getPreferredSources());
        }
        if(preferencesDTO.getPreferredCountries() != null) {
            existingPreferences.setPreferredCountries(preferencesDTO.getPreferredCountries());
        }
        NewsPreferences updatedPreferences = _preferencesRepository.save(existingPreferences);
        return NewsPreferencesDTO.builder()
                .userId(updatedPreferences.getUser().getId())
                .username(updatedPreferences.getUser().getUsername())
                .preferredLanguage(updatedPreferences.getPreferredLanguage())
                .preferredCategories(updatedPreferences.getPreferredCategories())
                .preferredSources(updatedPreferences.getPreferredSources())
                .preferredCountries(updatedPreferences.getPreferredCountries())
                .build();
    }

    @Override
    public String deletePreferences(Long userId) {
        User user = _userService.getUserEntityById(userId);
        NewsPreferences existingPreferences = user.getNewsPreferences();
        if (existingPreferences == null) {
            throw new DuplicateResourceException("Preferences do not exist for this user");
        }
        _preferencesRepository.delete(existingPreferences);
        user.setNewsPreferences(null);
        _userRepository.save(user);
        return "Preferences deleted successfully";
    }

    @Override
    public NewsPreferencesDTO getPreferences(Long userId) {
        User user = _userService.getUserEntityById(userId);
        NewsPreferences existingPreferences = user.getNewsPreferences();
        if (existingPreferences == null) {
            throw new ResourceNotFoundException("Preferences do not exist for this user");
        }
        return NewsPreferencesDTO.builder()
                .userId(existingPreferences.getUser().getId())
                .username(existingPreferences.getUser().getUsername())
                .preferredLanguage(existingPreferences.getPreferredLanguage())
                .preferredCategories(existingPreferences.getPreferredCategories())
                .preferredSources(existingPreferences.getPreferredSources())
                .preferredCountries(existingPreferences.getPreferredCountries())
                .build();
    }

    @Override
    public NewsPreferencesDTO setLanguage(Long userId, String language) {
        User user = _userService.getUserEntityById(userId);
        NewsPreferences existingPreferences = user.getNewsPreferences();
        if (existingPreferences == null) {
            throw new ResourceNotFoundException("Preferences do not exist for this user");
        }
        existingPreferences.setPreferredLanguage(language);
        NewsPreferences updatedPreferences = _preferencesRepository.save(existingPreferences);
        return NewsPreferencesDTO.builder()
                .userId(updatedPreferences.getUser().getId())
                .username(updatedPreferences.getUser().getUsername())
                .preferredLanguage(updatedPreferences.getPreferredLanguage())
                .preferredCategories(updatedPreferences.getPreferredCategories())
                .preferredSources(updatedPreferences.getPreferredSources())
                .preferredCountries(updatedPreferences.getPreferredCountries())
                .build();
    }
}
