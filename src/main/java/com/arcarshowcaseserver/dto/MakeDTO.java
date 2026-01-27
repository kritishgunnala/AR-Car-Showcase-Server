package com.arcarshowcaseserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MakeDTO {
    @JsonProperty("make_id")
    private String makeId;
    
    @JsonProperty("make_display")
    private String makeDisplay;
    
    @JsonProperty("make_is_common")
    private String makeIsCommon;
    
    @JsonProperty("make_country")
    private String makeCountry;
}
