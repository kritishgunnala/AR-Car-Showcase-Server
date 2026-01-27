package com.arcarshowcaseserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CarQueryResponse<T> {
    @JsonProperty("Makes")
    private T makes;
    
    @JsonProperty("Models")
    private T models;
    
    @JsonProperty("Trims")
    private T trims;
}
