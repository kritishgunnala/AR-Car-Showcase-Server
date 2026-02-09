package com.arcarshowcaseserver.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "car")
public class CarModelConfig {

    private Map<String, ModelEntry> models = new HashMap<>();

    public Map<String, ModelEntry> getModels() {
        return models;
    }

    public void setModels(Map<String, ModelEntry> models) {
        this.models = models;
    }

    public static class ModelEntry {
        private String brand;
        private List<String> modelNames;
        private String file;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public List<String> getModelNames() {
            return modelNames;
        }

        public void setModelNames(List<String> modelNames) {
            this.modelNames = modelNames;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }
    }
}
