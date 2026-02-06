package com.arcarshowcaseserver.configuration;

import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.service.CarImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private final CarRepository carRepository;
    private final CarImportService carImportService;

    public DataSeeder(CarRepository carRepository,
                      CarImportService carImportService) {
        this.carRepository = carRepository;
        this.carImportService = carImportService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (carRepository.count() == 0) {
            System.out.println(">>> Starting Car Data Seed  (Literal Mapping)...");
            try (java.io.InputStream is = new org.springframework.core.io.ClassPathResource("cars_data_final.json")
                    .getInputStream()) {
                carImportService.importData(is);
                System.out.println(">>> Car Data Seed  COMPLETED!");
            } catch (Exception e) {
                System.err.println(">>> Car Data Seed  FAILED: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> Car data  already exists. Skipping seed.");
        }
    }
}
