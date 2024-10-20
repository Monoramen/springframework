package com.springdemo.dto;

import java.util.ArrayList;
import java.util.List;

public class ChatDto {

  private Long id;

  private String chatName;

  private List<MessageDto> messages = new ArrayList<>();

  private List<UserInfoDto> participants = new ArrayList<>();

  public ChatDto(Long id, String chatName, List<UserInfoDto> participants) {
    this.id = id;
    this.chatName = chatName != null ? chatName : "";
    this.participants = participants != null ? participants : new ArrayList<>();
  }

  public ChatDto() {
    this.participants = new ArrayList<>();
    this.messages = new ArrayList<>();
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

  public List<MessageDto> getMessages() {
    return messages;
  }

  public void setMessages(List<MessageDto> messages) {
    this.messages = messages;
  }

  public List<UserInfoDto> getParticipants() {
    return participants;
  }

  public void setParticipants(List<UserInfoDto> participants) {
    this.participants = participants;
  }


  public List<Long> participants() {
    List<Long> participantsId =  new ArrayList<>();
    for(UserInfoDto user : participants){
      participantsId.add(user.getId());
    }
    return  participantsId;
  }

  @Override
  public String toString() {
    return "ChatDto{" +
        "id=" + id +
        ", chatName='" + chatName + '\'' +
        ", messages=" + messages +
        ", participants=" + (participants != null ? participants() : "") +
        '}';
  }
}
