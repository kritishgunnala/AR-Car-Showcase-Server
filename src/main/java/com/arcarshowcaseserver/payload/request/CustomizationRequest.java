package com.arcarshowcaseserver.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CustomizationRequest {
    private String vehicleId;
    private Map<String, String> materials;
}
