package com.springdemo.dto;

import java.util.ArrayList;
import java.util.List;

public class CreateChatDto {

  private Long id;

  private String chatName;

  private List<Long> participants;

  public CreateChatDto(String chatName, List<Long> participants) {
    this.chatName = chatName;
    this.participants = participants;
  }
  public CreateChatDto(List<Long> participants) {
    this.participants = participants;
  }

  public CreateChatDto() {
    this.participants = new ArrayList<>();
  }

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

  public List<Long> getParticipants() {
    return participants;
  }

  public void setParticipants(List<Long> participants) {
    this.participants = participants;
  }

  @Override
  public String toString() {
    return "ChatDto{" +
        "id=" + id +
        ", chatName='" + chatName + '\'' +
        ", participants=" + participants +
        '}';
  }
}
