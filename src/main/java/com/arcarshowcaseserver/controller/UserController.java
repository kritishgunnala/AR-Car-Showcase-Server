package com.arcarshowcaseserver.controller;

import com.arcarshowcaseserver.dto.UserPreferencesDTO;
import com.arcarshowcaseserver.model.User;
import com.arcarshowcaseserver.repository.UserRepository;
import com.arcarshowcaseserver.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserPreferencesDTO profileDTO) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findById(userDetails.getId()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (profileDTO.getFavBrands() != null) {
            user.setFavBrands(new HashSet<>(profileDTO.getFavBrands()));
        }
        if (profileDTO.getPreferredBodyTypes() != null) {
            user.setPreferredBodyTypes(new HashSet<>(profileDTO.getPreferredBodyTypes()));
        }
        if (profileDTO.getPreferredFuelTypes() != null) {
            user.setPreferredFuelTypes(new HashSet<>(profileDTO.getPreferredFuelTypes()));
        }
        if (profileDTO.getPreferredTransmissions() != null) {
            user.setPreferredTransmissions(new HashSet<>(profileDTO.getPreferredTransmissions()));
        }
        if (profileDTO.getDrivingCondition() != null) user.setDrivingCondition(profileDTO.getDrivingCondition());
        if (profileDTO.getMaxBudget() != null) user.setMaxBudget(profileDTO.getMaxBudget());

        userRepository.save(user);
        return ResponseEntity.ok(new com.arcarshowcaseserver.payload.response.MessageResponse("Profile updated successfully"));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userRepository.findById(userDetails.getId()).orElse(null);
         if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
