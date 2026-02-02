package com.arcarshowcaseserver.controller;

import com.arcarshowcaseserver.payload.request.CustomizationRequest;
import com.arcarshowcaseserver.service.CustomizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customizations")
@RequiredArgsConstructor
public class CustomizationController {

    private final CustomizationService customizationService;

    @PostMapping
    public ResponseEntity<?> createCustomization(@RequestBody CustomizationRequest request) {
        return ResponseEntity.ok(customizationService.createCustomization(request));
    }

    @GetMapping
    public ResponseEntity<?> getUserCustomizations() {
        return ResponseEntity.ok(customizationService.getUserCustomizations());
    }
}
