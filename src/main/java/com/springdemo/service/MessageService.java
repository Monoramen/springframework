package com.springdemo.service;

import com.ctc.wstx.shaded.msv_core.scanner.dtd.MessageCatalog;
import com.springdemo.dto.MessageDto;
import com.springdemo.entity.Chat;
import com.springdemo.entity.Message;
import com.springdemo.entity.User;
import com.springdemo.exception.NotFoundException;
import com.springdemo.mapper.MessageMapper;
import com.springdemo.repository.ChatRepository;
import com.springdemo.repository.MessageRepository;
import com.springdemo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageService {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ChatRepository chatRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  MessageMapper messageMapper;

  public List<MessageDto> findAll() {
    List<Message> messageList = messageRepository.findAll();
    return messageList.stream().map(messageMapper::toDTO).collect(Collectors.toList());
  }

  public MessageDto findById(Long id) {
    Message message =
        messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Message with id " + id + " not found"));
    return messageMapper.toDTO(message);
  }

  public MessageDto save(MessageDto messageDto) {
    User sender = userRepository.findById(messageDto.getSenderId())
        .orElseThrow(() -> new NotFoundException("User with id " + messageDto.getSenderId() + " not found."));

    Chat chat = chatRepository.findById(messageDto.getChatId())
        .orElseThrow(() -> new NotFoundException("Chat with id " + messageDto.getChatId() + " not found."));

    // Проверяем, является ли отправитель участником чата
    if (!chat.getParticipants().contains(sender)) {
      throw new IllegalArgumentException("User with id " + sender.getId() + " is not a participant in chat with id " + chat.getId());
    }

    Message message = new Message();
    message.setMessage(messageDto.getMessage());
    message.setSender(sender);
    message.setChat(chat);

    Message savedMessage = messageRepository.save(message);

    return messageMapper.toDTO(savedMessage);
  }

  public MessageDto update(Long id,MessageDto messageDto) {
    Message message = messageRepository.findById(id).orElseThrow(() -> new NotFoundException("Message with id " + id + " not found."));

    message.setMessage(messageDto.getMessage());

    Message savedMessage = messageRepository.save(message);

    return messageMapper.toDTO(savedMessage);
  }


  public void delete(Long id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Message with id " + id + " not found"));
    messageRepository.delete(message);
  }

}
