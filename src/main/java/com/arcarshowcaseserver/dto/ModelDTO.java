package com.arcarshowcaseserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ModelDTO {
    @JsonProperty("model_name")
    private String modelName;
    
    @JsonProperty("model_make_id")
    private String modelMakeId;
}
