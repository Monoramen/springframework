package com.springdemo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "chats")
public class Chat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String chatName;

  @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  @ManyToMany(mappedBy = "chats", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<User> participants = new ArrayList<>();

  public Chat(String chatName) {
    this.chatName = chatName;
  }

  public Chat() {
  }

  public void addParticipant(User user) {
    this.participants.add(user);
  }

  public List<Message> getMessages() {
    return messages;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setChatName(String chatName) {
    this.chatName = chatName;
  }

  public String getChatName() {
    return chatName;
  }

  public List<User> getParticipants() {
    return participants;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public void setParticipants(List<User> participants) {
    this.participants = participants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Chat chat = (Chat) o;
    return Objects.equals(id, chat.id) && Objects.equals(chatName, chat.chatName)
        ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, chatName);
  }

  @Override
  public String toString() {
    return "Chat{" +
        "id=" + id +
        ", chatName='" + chatName + '\'' +
        ", messages=" + (messages != null ? getMessages() : 0) +
        ", participants=" + (participants != null ? participants.size() : 0) +
        '}';
  }
}
