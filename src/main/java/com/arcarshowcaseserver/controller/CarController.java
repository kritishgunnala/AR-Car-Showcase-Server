package com.arcarshowcaseserver.controller;

import com.arcarshowcaseserver.dto.MakeDTO;
import com.arcarshowcaseserver.dto.ModelDTO;
import com.arcarshowcaseserver.dto.TrimDTO;
import com.arcarshowcaseserver.dto.YearDTO;
import com.arcarshowcaseserver.service.CarService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
public class CarController {

    private final CarService carService;

    @GetMapping("/years")
    public YearDTO getYears() {
        System.out.println("Controller: getYears called");
        return carService.getYears();
    }

    @GetMapping("/makes")
    public List<MakeDTO> getMakes(
            @RequestParam(required = false) @Min(1900) @Max(2100) Integer year) {
        return carService.getMakes(year);
    }

    @GetMapping("/models")
    public List<ModelDTO> getModels(
            @RequestParam String make,
            @RequestParam(required = false) @Min(1900) @Max(2100) Integer year) {
        return carService.getModels(make, year);
    }

    @GetMapping("/trims")
    public List<TrimDTO> getTrims(
            @RequestParam String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) @Min(1900) @Max(2100) Integer year) {
        System.out.println("Controller: getTrims called for make=" + make + ", model=" + model + ", year=" + year);
        return carService.getTrims(make, model, year);
    }
}
