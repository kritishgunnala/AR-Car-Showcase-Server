package com.arcarshowcaseserver.service.serviceImpl;

import com.arcarshowcaseserver.model.Cars.Car;
import com.arcarshowcaseserver.model.Cars.CarVariant;
import com.arcarshowcaseserver.dto.CarDTO;
import com.arcarshowcaseserver.exceptions.ResourceNotFoundException;
import com.arcarshowcaseserver.repository.CarRepository;
import com.arcarshowcaseserver.repository.CarVariantRepository;
import com.arcarshowcaseserver.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    private static final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);
    private final CarVariantRepository carVariantRepository;
    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository, CarVariantRepository carVariantRepository){
        this.carRepository = carRepository;
        this.carVariantRepository = carVariantRepository;
    }

    @Override
    public List<Car> GetAllCars() {
        return carRepository.findAll();

    }

    @Override
    public List<Car> searchCars(String keyword) {
        return List.of();
    }

    @Override
    public ResponseEntity<Car> getCarsById(Long id) {
        Optional<Car> cars  = carRepository.findById(id);
        if (cars.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found with Id: " +id
            );
    }
        return new ResponseEntity<>(cars.get(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllBrands() {
        return new ResponseEntity<>(carRepository.findAll()
                .stream()
                .map(Car::getBrand)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .distinct()
                .sorted()
                .toList(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllModels(String brand) {
        List<String> carsBrands = carRepository.findModelsByBrandIgnoreCase(brand);
        log.debug("Found {} models for brand {}", carsBrands.size(), brand);
        if (carsBrands.isEmpty()){
            return new ResponseEntity<>(String.format("No Model Found in %s",brand) , HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carsBrands,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllVariants(String brand, String model) {
    Optional<Car> car = carRepository.findByBrandAndModel(brand, model);
    if (car.isEmpty()){
        return new ResponseEntity<>("Not Found" , HttpStatus.NOT_FOUND);
    }
        List<CarVariant> carVariant = carVariantRepository.findVariantsByCarId(car.get().getId());
        if (carVariant == null){
            return new ResponseEntity<>(String.format("No Variants Found in %s " , model), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carVariant, HttpStatus.OK);    }



    @Override
    public ResponseEntity<?> getVariant(String brand, String model, String variant) {
        variant = getString(variant);
        Optional<Car> carOpt =
                carRepository.findByBrandAndModel(
                        brand.trim(),
                        model.trim()
                );
        if (carOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Car not found");
        }
        Optional<CarVariant> variantOpt =
                carVariantRepository.findByCarIdAndVariant(
                        carOpt.get().getId(),
                        variant
                );
        if (variantOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Invalid variant");
        }
        return ResponseEntity.ok(variantOpt.get());
    }

    @Override
    public ResponseEntity<List<CarDTO>> getByBodyType(String bodyType) {
        List<CarDTO> carDTO =
                carRepository.findByBodyType(bodyType.trim());
        if (carDTO.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No cars found for body type: " + bodyType
            );
        }
        return ResponseEntity.ok(carDTO);
    }



    @Override
    public ResponseEntity<List<CarDTO>> getByBrandAndBodyType(String brand, String bodyType) {
        List<CarDTO> carDTO =
                carRepository.findByBrandAndBodyType(brand,bodyType.trim());
        if (carDTO.isEmpty()) {
            throw new ResourceNotFoundException(
                    " No cars found in " +brand+" for body type: " + bodyType
            );
        }
        return ResponseEntity.ok(carDTO);
    }

    @Override
    public ResponseEntity<List<CarDTO>> getByFuelType(String fuelType) {
        List<CarDTO> carDTO =
                carRepository.findByFuelType(fuelType.trim());
        if (carDTO.isEmpty()) {
            throw new ResourceNotFoundException(
                    " No cars found with fuelType :" + fuelType
            );
        }
        return ResponseEntity.ok(carDTO);
    }

    @Override
    public ResponseEntity<List<CarDTO>> getBytransmissionType(String transmissionType) {
        List<CarDTO> carDTO =
                carRepository.findByTransmissionType(transmissionType.trim());
        if (carDTO.isEmpty()) {
            throw new ResourceNotFoundException(
                    " No cars found with transmissionType :" + transmissionType
            );
        }
        return ResponseEntity.ok(carDTO);
    }

    @Override
    public ResponseEntity<List<CarDTO>> getByRating(double rating) {
        List<CarDTO> carDTO =
                carRepository.findByRating(rating);
        if (carDTO.isEmpty()) {
            throw new ResourceNotFoundException(
                    " No cars found with rating :" + rating
            );
        }
        return ResponseEntity.ok(carDTO);
    }

    @Override
    public ResponseEntity<List<CarDTO>> getByPricing(double price) {
        List<CarDTO> carDTO = carRepository.findByUnderPrice(price);

        if (carDTO.isEmpty()) {
            throw new ResourceNotFoundException(
                    " No cars found under the price :" + price
            );
        }

        return ResponseEntity.ok(carDTO);
    }


    private String getString(String data){
        return data.replace("+", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    @Override
    public com.arcarshowcaseserver.dto.CarOptionsDTO getCarOptions() {
        return new com.arcarshowcaseserver.dto.CarOptionsDTO(
            carRepository.findDistinctBrands(),
            carRepository.findDistinctBodyTypes(),
            carRepository.findDistinctFuelTypes(),
            carRepository.findDistinctTransmissionTypes()
        );
    }
}



