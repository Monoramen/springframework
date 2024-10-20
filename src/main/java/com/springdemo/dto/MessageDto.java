package com.springdemo.dto;

public class MessageDto {

  private Long id;

  private String message;

  private Long senderId;  // Идентификатор пользователя

  private Long chatId;    // Идентификатор чата

  public MessageDto(Long id, String message, Long senderId, Long chatId) {
    this.id = id;
    this.message = message;
    this.senderId = senderId;
    this.chatId = chatId;
  }

  public MessageDto() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long senderId) {
    this.senderId = senderId;
  }

  public Long getChatId() {
    return chatId;
  }

  public void setChatId(Long chatId) {
    this.chatId = chatId;
  }

  @Override
  public String toString() {
    return "MessageDto{" +
        "id=" + id +
        ", message='" + message + '\'' +
        ", senderId=" + senderId +
        ", chatId=" + chatId +
        '}';
  }
}
