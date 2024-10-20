package com.springdemo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "users_chats",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "chat_id")
  )
  private List<Chat> chats = new ArrayList<>();

  public void addChat(Chat chat) {
    chats.add(chat);
    chat.getParticipants().add(this);
  }

  public void remove(Chat chat) {
    if (chat != null) {
      chats.remove(chat);
      chat.getParticipants().remove(this);
    }
  }

  public User(String username, String pass) {
    this.username = username;
    this.password = pass;
  }

  public User() {
  }

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Chat> getChats() {
    return chats;
  }

  public void setChats(List<Chat> chats) {
    this.chats = chats;
  }

  public List<Chat> chatList() {
    List<Chat> cutChatInfo = new ArrayList<>();
    Chat chatInfo = new Chat();
    for (Chat chat : this.chats) {
      chatInfo.setId(chat.getId());
      chatInfo.setChatName(chat.getChatName());
      chatInfo.setMessages(chat.getMessages());
      cutChatInfo.add(chatInfo);
    }
    return cutChatInfo;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof User)) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.username, user.username) &&
        Objects.equals(this.password, user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.username, this.password);
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", chats=" + (chats != null ? chatList() : "empty") +
        '}';
  }

}