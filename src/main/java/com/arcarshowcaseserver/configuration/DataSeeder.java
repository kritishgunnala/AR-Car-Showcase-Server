package com.arcarshowcaseserver.configuration;

import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.service.CarImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private final CarRepository carRepository;
    private final CarImportService carImportService;

    public DataSeeder(CarRepository carRepository,
                      CarImportService carImportService) {
        this.carRepository = carRepository;
        this.carImportService = carImportService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (carRepository.count() > 0) {
            log.info(">>> Car data already exists. Skipping seed.");
            return;
        }
        
        log.info(">>> Starting Car Data Seed (Literal Mapping + Configured Models)...");
        try (java.io.InputStream is = new org.springframework.core.io.ClassPathResource("cars_data_final.json")
                .getInputStream()) {
            carImportService.importData(is);
            log.info(">>> Car Data Seed COMPLETED!");
        } catch (Exception e) {
            log.error(">>> Car Data Seed FAILED: {}", e.getMessage());
        }
    }
}
