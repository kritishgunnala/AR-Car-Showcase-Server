package com.arcarshowcaseserver.dto;

public class InteractionDTO {

    private Long carId;
    private String action;

    public InteractionDTO() {}

    public InteractionDTO(Long carId, String action) {
        this.carId = carId;
        this.action = action;
    }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
