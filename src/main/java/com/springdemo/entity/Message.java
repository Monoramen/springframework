package com.springdemo.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat", nullable = false)
  @NotNull
  private Chat chat;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender", nullable = false)
  @NotNull
  private User sender;

  public Message(String message, Chat chat, User sender) {
    this.message = message;
    this.chat = chat;
    this.sender = sender;
  }

  public Message() {
  }

  public User getSender() {
    return sender;
  }

  public void setSender(User sender) {
    this.sender = sender;
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

  public Chat getChat() {
    return chat;
  }

  public void setChat(Chat chat) {
    this.chat = chat;
  }

  @Override
  public boolean equals(Object o) {
      if (this == o) {
          return true;
      }
      if (o == null || getClass() != o.getClass()) {
          return false;
      }
    Message message1 = (Message) o;
    return Objects.equals(id, message1.id) && Objects.equals(message, message1.message)
        && Objects.equals(chat.getId(), message1.chat.getId()) && Objects.equals(sender.getId(), message1.sender.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, message, chat.getId(), sender.getId());
  }

  @Override
  public String toString() {
    return "Message{" +
        "id=" + id +
        ", message='" + message + '\'' +
        ", chatId=" + (chat != null ? chat.getId() : 0) +
        ", senderId=" + (sender != null ? sender.getId() : 0) +
        '}';
  }
}
