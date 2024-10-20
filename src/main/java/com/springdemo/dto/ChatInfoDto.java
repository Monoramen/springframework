package com.springdemo.dto;

import java.util.ArrayList;
import java.util.List;

public class ChatInfoDto {

  private Long id;

  private String chatName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getChatName() {
    return chatName;
  }

  public void setChatName(String chatName) {
    this.chatName = chatName;
  }

  @Override
  public String toString() {
    return "ChatInfoDto{" +
        "id=" + id +
        ", chatName='" + chatName + '\'' +
        '}';
  }
}
