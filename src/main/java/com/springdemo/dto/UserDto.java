package com.springdemo.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto {

  private Long id;

  private String username;

  private String password;

  private List<ChatInfoDto> chats = new ArrayList<>();

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserDto() {}


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<ChatInfoDto> getChats() {
    return chats;
  }

  public void setChats(List<ChatInfoDto> chats) {
    this.chats = chats;
  }


  @Override
  public String toString() {
    return "UserDto{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", chats=" + (chats != null ? chats: "")  +
        '}';
  }

}