package com.arcarshowcaseserver.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String email;
  private List<String> roles;
  private String phoneNumber;
  private String profilePic;

  private java.util.Set<String> favBrands;
  private java.util.Set<String> preferredBodyTypes;
  private java.util.Set<String> preferredFuelTypes;
  private java.util.Set<String> preferredTransmissions;
  private String drivingCondition;
  private Double maxBudget;

  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, String phoneNumber, String profilePic,
                     java.util.Set<String> favBrands, java.util.Set<String> preferredBodyTypes,
                     java.util.Set<String> preferredFuelTypes, java.util.Set<String> preferredTransmissions,
                     String drivingCondition, Double maxBudget) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.phoneNumber = phoneNumber;
    this.profilePic = profilePic;
    this.favBrands = favBrands;
    this.preferredBodyTypes = preferredBodyTypes;
    this.preferredFuelTypes = preferredFuelTypes;
    this.preferredTransmissions = preferredTransmissions;
    this.drivingCondition = drivingCondition;
    this.maxBudget = maxBudget;
  }
}
