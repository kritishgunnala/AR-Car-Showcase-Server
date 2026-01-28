package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.dto.MakeDTO;
import com.arcarshowcaseserver.dto.ModelDTO;
import com.arcarshowcaseserver.dto.TrimDTO;
import com.arcarshowcaseserver.dto.YearDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarQueryClient carQueryClient;

    public YearDTO getYears() {
        return carQueryClient.getYears();
    }

    public List<MakeDTO> getMakes(Integer year) {
        return carQueryClient.getMakes(year);
    }

    public List<ModelDTO> getModels(String make, Integer year) {
        return carQueryClient.getModels(make, year);
    }

    public List<TrimDTO> getTrims(String make, String model, Integer year) {
        return carQueryClient.getTrims(make, model, year);
    }
}
