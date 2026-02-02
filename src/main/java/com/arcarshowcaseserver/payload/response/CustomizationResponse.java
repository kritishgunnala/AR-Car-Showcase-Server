package com.arcarshowcaseserver.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CustomizationResponse {
    private UUID customizationId;
    private String modelUrl;
}
