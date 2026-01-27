package com.arcarshowcaseserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TrimDTO {
    @JsonProperty("model_id")
    private String modelId;
    
    @JsonProperty("model_make_id")
    private String modelMakeId;
    
    @JsonProperty("model_name")
    private String modelName;
    
    @JsonProperty("model_trim")
    private String modelTrim;
    
    @JsonProperty("model_year")
    private String modelYear;
    
    @JsonProperty("model_body")
    private String modelBody;
    
    @JsonProperty("model_engine_position")
    private String modelEnginePosition;
    
    @JsonProperty("model_engine_cc")
    private String modelEngineCc;
    
    @JsonProperty("model_engine_num_cyl")
    private String modelEngineNumCyl;
    
    @JsonProperty("model_engine_type")
    private String modelEngineType;
    
    @JsonProperty("model_engine_valves_per_cyl")
    private String modelEngineValvesPerCyl;
    
    @JsonProperty("model_engine_power_ps")
    private String modelEnginePowerPs;
    
    @JsonProperty("model_engine_power_rpm")
    private String modelEnginePowerRpm;
    
    @JsonProperty("model_engine_torque_nm")
    private String modelEngineTorqueNm;
    
    @JsonProperty("model_engine_torque_rpm")
    private String modelEngineTorqueRpm;
    
    @JsonProperty("model_engine_bore_mm")
    private String modelEngineBoreMm;
    
    @JsonProperty("model_engine_stroke_mm")
    private String modelEngineStrokeMm;
    
    @JsonProperty("model_engine_compression")
    private String modelEngineCompression;
    
    @JsonProperty("model_engine_fuel")
    private String modelEngineFuel;
    
    @JsonProperty("model_top_speed_kph")
    private String modelTopSpeedKph;
    
    @JsonProperty("model_0_to_100_kph")
    private String model0To100Kph;
    
    @JsonProperty("model_drive")
    private String modelDrive;
    
    @JsonProperty("model_transmission_type")
    private String modelTransmissionType;
    
    @JsonProperty("model_seats")
    private String modelSeats;
    
    @JsonProperty("model_doors")
    private String modelDoors;
    
    @JsonProperty("model_weight_kg")
    private String modelWeightKg;
    
    @JsonProperty("model_length_mm")
    private String modelLengthMm;
    
    @JsonProperty("model_width_mm")
    private String modelWidthMm;
    
    @JsonProperty("model_height_mm")
    private String modelHeightMm;
    
    @JsonProperty("model_wheelbase_mm")
    private String modelWheelbaseMm;
    
    @JsonProperty("model_lkm_hwy")
    private String modelLkmHwy;
    
    @JsonProperty("model_lkm_mixed")
    private String modelLkmMixed;
    
    @JsonProperty("model_lkm_city")
    private String modelLkmCity;
    
    @JsonProperty("model_fuel_cap_l")
    private String modelFuelCapL;
    
    @JsonProperty("model_sold_in_us")
    private String modelSoldInUs;
    
    @JsonProperty("model_co2")
    private String modelCo2;
    
    @JsonProperty("model_make_display")
    private String modelMakeDisplay;
}
