package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.RecommendationHistory;
import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.repository.RecommendationHistoryRepository;
import com.arcarshowcaseserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecommendationHistoryRepository historyRepository;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    @Value("${ml.service.key}")
    private String mlServiceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private String toBrandModelKey(Car car) {
        return (car.getBrand() + "_" + car.getModel()).toLowerCase();
    }

    public List<Car> getRecommendedCars(Long carId) {
        Optional<Car> carOptional = carRepository.findById(carId);
        if (carOptional.isEmpty()) {
            return new ArrayList<>();
        }
        Car currentCar = carOptional.get();
        String modelName = currentCar.getModel();

        String url = mlServiceUrl + "/recommend/similar/" + modelName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-ML-KEY", mlServiceKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
            );

            if (response.getBody() == null) {
                return new ArrayList<>();
            }

            List<Map<String, String>> recommendedItems = (List<Map<String, String>>) response.getBody();
            List<Car> recommendedCars = new ArrayList<>();

            for (Map<String, String> item : recommendedItems) {
                String brand = item.get("brand");
                String model = item.get("model");
                Optional<Car> recCar = carRepository.findByBrandAndModel(brand, model);
                recCar.ifPresent(recommendedCars::add);
            }

            return recommendedCars;

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Car> getPersonalizedRecommendations(Long userId) {
        com.arcarshowcaseserver.model.User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new ArrayList<>();
        }

        List<String> brands = new ArrayList<>(user.getFavBrands() != null ? user.getFavBrands() : new ArrayList<>());
        List<String> bodyTypes = new ArrayList<>(user.getPreferredBodyTypes() != null ? user.getPreferredBodyTypes() : new ArrayList<>());
        List<String> fuelTypes = new ArrayList<>(user.getPreferredFuelTypes() != null ? user.getPreferredFuelTypes() : new ArrayList<>());
        List<String> transmissions = new ArrayList<>(user.getPreferredTransmissions() != null ? user.getPreferredTransmissions() : new ArrayList<>());

        brands.replaceAll(String::toLowerCase);
        bodyTypes.replaceAll(String::toLowerCase);
        fuelTypes.replaceAll(String::toLowerCase);
        transmissions.replaceAll(String::toLowerCase);

        String drivingCondition = user.getDrivingCondition() != null ? user.getDrivingCondition() : "";
        Double maxBudget = user.getMaxBudget() != null ? user.getMaxBudget() : 1000.0;

        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<Long> shownCarIds = historyRepository.findShownCarIdsByUserSince(userId, since);
        List<String> excludeKeys = new ArrayList<>();
        for (Long shownId : shownCarIds) {
            Optional<Car> shownCar = carRepository.findById(shownId);
            shownCar.ifPresent(car -> excludeKeys.add(toBrandModelKey(car)));
        }

        String url = mlServiceUrl + "/recommend/personalized";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-ML-KEY", mlServiceKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("brands", brands);
        body.put("body_types", bodyTypes);
        body.put("fuel_types", fuelTypes);
        body.put("transmissions", transmissions);
        body.put("budget", maxBudget);
        body.put("driving_condition", drivingCondition);
        body.put("exclude_car_ids", excludeKeys);
        body.put("user_id", String.valueOf(userId));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                List.class
            );

            if (response.getBody() == null) {
                return new ArrayList<>();
            }

            List<Map<String, String>> recommendedItems = (List<Map<String, String>>) response.getBody();
            List<Car> recommendedCars = new ArrayList<>();

            for (Map<String, String> item : recommendedItems) {
                String rBrand = item.get("brand");
                String rModel = item.get("model");
                Optional<Car> found = carRepository.findByBrandAndModel(rBrand, rModel);
                found.ifPresent(recommendedCars::add);
            }

            return recommendedCars;

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void recordInteraction(Long userId, Long carId, String action) {
        RecommendationHistory history = new RecommendationHistory(userId, carId, null);
        history.setAction(action);
        historyRepository.save(history);

        Optional<Car> car = carRepository.findById(carId);
        car.ifPresent(c -> relayFeedbackToPython(toBrandModelKey(c), action));
    }

    private void relayFeedbackToPython(String carKey, String action) {
        String url = mlServiceUrl + "/recommend/feedback";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-ML-KEY", mlServiceKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("car_id", carKey);
        body.put("action", action);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        } catch (Exception ignored) {
        }
    }
}
