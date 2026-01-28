package com.arcarshowcaseserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class YearDTO {
    @JsonProperty("min_year")
    private String minYear;
    
    @JsonProperty("max_year")
    private String maxYear;
}
