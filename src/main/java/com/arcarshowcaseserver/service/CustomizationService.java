package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.model.Customization;
import com.arcarshowcaseserver.model.User;
import com.arcarshowcaseserver.payload.request.CustomizationRequest;
import com.arcarshowcaseserver.payload.response.CustomizationResponse;
import com.arcarshowcaseserver.repository.CustomizationRepository;
import com.arcarshowcaseserver.repository.UserRepository;
import com.arcarshowcaseserver.security.services.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomizationService {

    private final CustomizationRepository customizationRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${blender.service.url}")
    private String blenderServiceUrl;

    @Transactional
    public CustomizationResponse createCustomization(CustomizationRequest request) {
        User user = getCurrentUser();

        Customization customization = new Customization();
        customization.setUser(user);
        customization.setVehicleId(request.getVehicleId());
        try {
            customization.setMaterials(objectMapper.writeValueAsString(request.getMaterials()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting materials to JSON", e);
        }
        customizationRepository.save(customization);

        String generateUrl = blenderServiceUrl + "/generate";
        
        Map<String, Object> pythonRequest = new HashMap<>();
        pythonRequest.put("base_model", "car.glb"); 
        pythonRequest.put("materials", request.getMaterials());
        pythonRequest.put("output_name", "car_" + customization.getId().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(pythonRequest, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(generateUrl, entity, Map.class);
        
        if (response.getBody() == null || !response.getBody().containsKey("model_url")) {
            throw new RuntimeException("Error from Blender service: " + response.getBody());
        }

        String modelFilename = (String) response.getBody().get("model_url");

        String fullModelUrl = "/api/models/" + modelFilename;
        customization.setModelUrl(fullModelUrl);
        customizationRepository.save(customization);

        return new CustomizationResponse(customization.getId(), fullModelUrl);
    }

    public List<CustomizationResponse> getUserCustomizations() {
        User user = getCurrentUser();
        return customizationRepository.findByUser(user).stream()
                .map(c -> new CustomizationResponse(c.getId(), c.getModelUrl()))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
