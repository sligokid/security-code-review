package com.land.registry.auth.model.dto;

public class UserProfile {

  private Long id;

  private String username;
  private String name;
  private String email;

  public UserProfile(final Long id, final String username, final String name, final String email) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

}
