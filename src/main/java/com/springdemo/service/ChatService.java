package com.springdemo.service;

import com.springdemo.controller.ChatController;
import com.springdemo.dto.ChatDto;
import com.springdemo.dto.CreateChatDto;
import com.springdemo.dto.UserDto;
import com.springdemo.dto.UserInfoDto;
import com.springdemo.entity.Chat;
import com.springdemo.entity.Message;
import com.springdemo.entity.User;
import com.springdemo.exception.NotFoundException;
import com.springdemo.mapper.ChatMapper;
import com.springdemo.mapper.UserMapper;
import com.springdemo.repository.ChatRepository;
import com.springdemo.repository.UserRepository;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {

  private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

  @Autowired
  private ChatRepository chatRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChatMapper chatMapper;

  @Autowired
  private UserMapper userMapper;

  public List<ChatDto> findAll() {
    List<Chat> chatList = chatRepository.findAll();
    return chatList.stream()
        .map(chatMapper::toDTO)
        .collect(Collectors.toList());
  }

  public ChatDto findById(Long id) {
    Chat chat = chatRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Chat with id " + id + " not found"));
    List<Message> msgs = chat.getMessages();
    System.out.println(msgs);
    return chatMapper.toDTO(chat);
  }

  public ChatDto save(CreateChatDto createChatDto) {

    List<Long> participantsId = createChatDto.getParticipants();
    Chat chat = new Chat();
    chat.setChatName(createChatDto.getChatName());
    Chat savedChat = chatRepository.save(chat);

    List<User> participants = userRepository.findAllById(participantsId);
    for (User user : participants) {
      user.addChat(savedChat);
    }
    userRepository.saveAll(participants);

    return chatMapper.toDTO(savedChat);
  }

  public ChatDto update(ChatDto chatDto) {
    Chat chat = new Chat();
    chat.setId(chatDto.getId());
    chat.setChatName(chatDto.getChatName());

   Chat chatFromRepo = chatRepository.findById(chat.getId())
        .orElseThrow(() -> new NotFoundException("Chat with id " + chat.getId() + " not found"));

    chat.setParticipants(chatFromRepo.getParticipants());
    return chatMapper.toDTO(chatRepository.save(chat));
  }

  public void delete(Long id) {
    Chat chat = chatRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Chat with id " + id + " not found"));

    List<User> userList = new ArrayList<>(chat.getParticipants());

    if (userList.isEmpty()) {
      throw new IllegalStateException("Cannot delete chat with no participants.");
    }

    Iterator<User> iterator = userList.iterator();
    while (iterator.hasNext()) {
      User user = iterator.next();
      if (user != null) {
        user.remove(chat); // Ensure this does not modify userList
      }
    }

    userRepository.saveAll(userList);
    chatRepository.delete(chat);
  }



}
