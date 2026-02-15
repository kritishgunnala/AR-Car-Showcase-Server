package com.arcarshowcaseserver.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserPreferencesDTO {
    private List<String> favBrands;
    private List<String> preferredBodyTypes;
    private List<String> preferredFuelTypes;
    private List<String> preferredTransmissions;
    private String drivingCondition;
    private Double maxBudget;
}
