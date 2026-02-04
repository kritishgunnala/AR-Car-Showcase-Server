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

  public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles, String phoneNumber, String profilePic) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.phoneNumber = phoneNumber;
    this.profilePic = profilePic;
  }
}
