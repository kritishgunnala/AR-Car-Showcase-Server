package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.model.Cars.*;
import com.arcarshowcaseserver.repository.CarRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class CarImportService {

    private static final Logger log = LoggerFactory.getLogger(CarImportService.class);
    private final CarRepository carRepository;
    private final ObjectMapper objectMapper;
    private final com.arcarshowcaseserver.configuration.CarModelConfig carModelConfig;

    public CarImportService(CarRepository carRepository, ObjectMapper objectMapper, com.arcarshowcaseserver.configuration.CarModelConfig carModelConfig) {
        this.carRepository = carRepository;
        this.objectMapper = objectMapper;
        this.carModelConfig = carModelConfig;
    }

    @Transactional
    public void importData(InputStream inputStream) throws IOException {
        JsonNode rootNode = objectMapper.readTree(inputStream);

        if (rootNode.isArray()) {
            List<Car> carsToSave = new ArrayList<>();
            for (JsonNode carNode : rootNode) {
                Car car = mapJsonToCar(carNode);
                carsToSave.add(car);
            }
            carRepository.saveAll(carsToSave);
        }
    }

    private Car mapJsonToCar(JsonNode node) {
        Car car = new Car();
        car.setBrand(node.path("brand").asText(""));
        car.setModel(node.path("model").asText(""));
        car.setBodyType(node.path("body_type").asText(""));
        car.setFuelType(node.path("fuel_type").asText(""));
        car.setTransmissionType(node.path("transmission_type").asText(""));
        car.setSeatingCapacity(node.path("seating_capacity").asInt(0));
        car.setPriceRange(node.path("price_range").asText(""));
        car.setMinPriceLakhs(node.path("min_price_lakhs").asDouble(0.0));
        car.setMaxPriceLakhs(node.path("max_price_lakhs").asDouble(0.0));
        car.setRating(node.path("rating").asDouble(0.0));

        String brand = car.getBrand();
        String model = car.getModel();
        String assignedModel = resolveModelFile(brand, model);
        
        car.setModelUrl("/api/static/models/" + assignedModel);

        JsonNode specsNode = node.path("specs");
        Iterator<Map.Entry<String, JsonNode>> specCategories = specsNode.fields();
        while (specCategories.hasNext()) {
            Map.Entry<String, JsonNode> categoryEntry = specCategories.next();
            String categoryName = categoryEntry.getKey();
            JsonNode categoryData = categoryEntry.getValue();

            Iterator<Map.Entry<String, JsonNode>> specFields = categoryData.fields();
            while (specFields.hasNext()) {
                Map.Entry<String, JsonNode> specEntry = specFields.next();
                CarDetail detail = new CarDetail(
                        categoryName,
                        specEntry.getKey(),
                        specEntry.getValue().asText(""),
                        car);
                car.getDetails().add(detail);
            }
        }

        JsonNode variantsNode = node.path("variants");
        if (variantsNode.isArray()) {
            for (JsonNode vNode : variantsNode) {
                CarVariant variant = new CarVariant();
                variant.setVariant(vNode.path("variant").asText(""));
                variant.setPrice(vNode.path("price").asText(""));
                variant.setEngineCc(vNode.path("engine_cc").asText(""));
                variant.setFuel(vNode.path("fuel").asText(""));
                variant.setTransmission(vNode.path("transmission").asText(""));
                variant.setMileage(vNode.path("mileage").asText(""));
                variant.setCar(car);

                JsonNode keySpecsNode = vNode.path("key_specifications");
                if (keySpecsNode.isArray()) {
                    List<String> keySpecs = new ArrayList<>();
                    for (JsonNode ks : keySpecsNode) {
                        keySpecs.add(ks.asText(""));
                    }
                    variant.setKeySpecifications(keySpecs);
                }
                car.getVariants().add(variant);
            }
        }

        JsonNode imagesNode = node.path("images");
        if (imagesNode.has("exterior")) {
            for (JsonNode n : imagesNode.get("exterior")) {
                car.getImages().add(new CarImage("EXTERIOR", n.asText(""), car));
            }
        }
        if (imagesNode.has("interior")) {
            for (JsonNode n : imagesNode.get("interior")) {
                car.getImages().add(new CarImage("INTERIOR", n.asText(""), car));
            }
        }
        if (imagesNode.has("colours")) {
            for (JsonNode n : imagesNode.get("colours")) {
               CarColor color = new CarColor();
                color.setName(n.path("name").asText(""));
                color.setImageUrl(n.path("image").asText(""));
                color.setCar(car);
                car.getColors().add(color);
            }
        }

        return car;
    }

    private String resolveModelFile(String brand, String model) {
        if (brand == null || model == null) return "car.glb";

        String normalizedBrand = brand.toLowerCase().replaceAll("[^a-z0-9]", "");

        for (com.arcarshowcaseserver.configuration.CarModelConfig.ModelEntry entry : carModelConfig.getModels().values()) {
            if (entry.getBrand() == null) continue;

            String configBrand = entry.getBrand().toLowerCase().replaceAll("[^a-z0-9]", "");
            
            if (normalizedBrand.equals(configBrand)) {
                String normalizedModel = model.toLowerCase().replaceAll("[^a-z0-9]", "");
                
                if (entry.getModelNames() != null) {
                    for (String configModelName : entry.getModelNames()) {
                        String normalizedConfigModel = configModelName.toLowerCase().replaceAll("[^a-z0-9]", "");
                        
                        log.debug("Checking: {} vs {}", normalizedModel, normalizedConfigModel);
                        if (normalizedModel.contains(normalizedConfigModel)) {
                            log.info("MATCHED! {}", entry.getFile());
                            return entry.getFile();
                        }
                    }
                }
            }
        }
        log.warn("NO MATCH for {} {} -> Defaulting to car.glb", brand, model);
        return "car.glb";
    }
}
