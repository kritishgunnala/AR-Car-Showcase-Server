package com.arcarshowcaseserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CarOptionsDTO {
    private List<String> brands;
    private List<String> bodyTypes;
    private List<String> fuelTypes;
    private List<String> transmissionTypes;
}
