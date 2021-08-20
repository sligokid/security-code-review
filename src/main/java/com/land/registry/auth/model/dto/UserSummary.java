package com.land.registry.auth.model.dto;

public class UserSummary {

  private Long id;

  private String username;
  private String name;

  public UserSummary(final Long id, final String username, final String name) {
    this.id = id;
    this.username = username;
    this.name = name;
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

}
