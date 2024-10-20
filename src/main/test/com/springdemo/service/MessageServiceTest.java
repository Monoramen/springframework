package com.springdemo.service;

import com.springdemo.dto.MessageDto;
import com.springdemo.entity.Chat;
import com.springdemo.entity.Message;
import com.springdemo.entity.User;
import com.springdemo.exception.NotFoundException;
import com.springdemo.mapper.MessageMapper;
import com.springdemo.repository.ChatRepository;
import com.springdemo.repository.MessageRepository;
import com.springdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChatRepository chatRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private MessageMapper messageMapper;

  @InjectMocks
  private MessageService messageService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void findAllListOfMessages() {
    Message message1 = new Message();
    message1.setId(1L);
    message1.setMessage("test msg");
    Message message2 = new Message();
    message2.setMessage("test2 msg");
    message2.setId(2L);

    when(messageRepository.findAll()).thenReturn(Arrays.asList(message1, message2));
    when(messageMapper.toDTO(message1)).thenReturn(new MessageDto());
    when(messageMapper.toDTO(message2)).thenReturn(new MessageDto());

    List<MessageDto> result = messageService.findAll();

    assertEquals(2, result.size());
    verify(messageRepository, times(1)).findAll();
    verify(messageMapper, times(2)).toDTO(any(Message.class));
  }

  @Test
  void findByIdMessage() {
    Message message = new Message();
    message.setId(1L);
    when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
    when(messageMapper.toDTO(message)).thenReturn(new MessageDto());

    MessageDto result = messageService.findById(1L);

    assertNotNull(result);
    verify(messageRepository, times(1)).findById(1L);
    verify(messageMapper, times(1)).toDTO(message);
  }

  @Test
  void findByIdThrowNotFoundException() {
    when(messageRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> messageService.findById(1L));
    assertEquals("Message with id 1 not found", exception.getMessage());
    verify(messageRepository, times(1)).findById(1L);
  }

  @Test
  void saveMessage() {
    MessageDto messageDto = new MessageDto();
    messageDto.setMessage("Hello");
    messageDto.setSenderId(1L);
    messageDto.setChatId(1L);

    User user = new User("test","test");
    user.setId(1L);
    Chat chat = new Chat("chat");
    chat.setId(1L);
    chat.setParticipants(new ArrayList<>(List.of(user)));

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
    when(messageRepository.save(any(Message.class))).thenReturn(new Message());
    when(messageMapper.toDTO(any(Message.class))).thenReturn(new MessageDto());

    MessageDto result = messageService.save(messageDto);

    assertNotNull(result);
    verify(messageRepository, times(1)).save(any(Message.class));
  }

  @Test
  void saveThrowNotFoundException() {
    MessageDto messageDto = new MessageDto();
    messageDto.setSenderId(1L);

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> messageService.save(messageDto));
    assertEquals("User with id 1 not found.", exception.getMessage());
    verify(userRepository, times(1)).findById(1L);
    verify(messageRepository, times(0)).save(any(Message.class));
  }

  @Test
  void deleteMessage() {
    Message message = new Message();
    message.setId(1L);
    when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

    messageService.delete(1L);

    verify(messageRepository, times(1)).findById(1L);
    verify(messageRepository, times(1)).delete(message);
  }

  @Test
  void deleteThrowNotFoundException() {
    when(messageRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> messageService.delete(1L));
    assertEquals("Message with id 1 not found", exception.getMessage());
    verify(messageRepository, times(1)).findById(1L);
    verify(messageRepository, times(0)).delete(any(Message.class));
  }


  @Test
  void updateMessage() {
    Long messageId = 1L;
    MessageDto messageDto = new MessageDto();
    messageDto.setId(messageId);
    messageDto.setMessage("Updated message");

    Message existingMessage = new Message();
    existingMessage.setId(messageId);
    existingMessage.setMessage("Old message");

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(existingMessage));

    existingMessage.setMessage(messageDto.getMessage());

    when(messageRepository.save(existingMessage)).thenReturn(existingMessage);

    when(messageMapper.toDTO(existingMessage)).thenReturn(messageDto);

    MessageDto result = messageService.update(messageId, messageDto);

    assertNotNull(result);
    assertEquals("Updated message", result.getMessage());

    verify(messageRepository, times(1)).findById(messageId);
    verify(messageRepository, times(1)).save(existingMessage);
  }


  @Test
  void updateThrowNotFoundException() {
    Long messageId = 1L;
    MessageDto messageDto = new MessageDto();
    messageDto.setMessage("Some message");

    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> messageService.update(messageId, messageDto));
    assertEquals("Message with id " + messageId + " not found.", exception.getMessage());

    verify(messageRepository, times(1)).findById(messageId);
    verify(messageRepository, times(0)).save(any(Message.class));
  }

}
