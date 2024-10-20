package com.springdemo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springdemo.dto.MessageDto;
import com.springdemo.entity.Chat;
import com.springdemo.entity.Message;
import com.springdemo.entity.User;
import com.springdemo.exception.NotFoundException;
import com.springdemo.service.MessageService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class MessageControllerTest {

  private MessageDto messageDto;

  private User user;

  private Chat chat;

  private Message message;

  @InjectMocks
  private MessageController messageController;

  @Mock
  private MessageService messageService;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    MockitoAnnotations.openMocks(this);

    user = new User();
    user.setId(1L);
    user.setUsername("Test User");

    chat = new Chat();
    chat.setId(1L);
    chat.setChatName("Test Chat");
    chat.setParticipants(List.of(user));

    messageDto = new MessageDto();
    messageDto.setId(1L);
    messageDto.setMessage("Test Message");
    messageDto.setSenderId(1L);
    messageDto.setChatId(1L);

    message = new Message();
    message.setId(1L);
    message.setMessage("Test Message");
    message.setSender(user);
    message.setChat(chat);

  }

  @Test
  void findAllListOfMessages() {
    when(messageService.findAll()).thenReturn(Collections.singletonList(messageDto));

    List<MessageDto> result = messageController.findAll();

    assertEquals(1, result.size());
    assertEquals(messageDto, result.get(0));
    verify(messageService, times(1)).findAll();
  }

  @Test
  void findByIdMessage() {
    when(messageService.findById(1L)).thenReturn(messageDto);

    MessageDto result = messageController.findById(1L);

    assertEquals(messageDto, result);
    verify(messageService, times(1)).findById(1L);
  }

  @Test
  void findByIdThrowNotFound() {
    when(messageService.findById(1L)).thenThrow(
        new NotFoundException("Message with id 1 not found"));

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> messageController.findById(1L));

    assertEquals("Message with id 1 not found", exception.getMessage());
    verify(messageService, times(1)).findById(1L);
  }

  @Test
  void saveUserSavedMessage() {
    when(messageService.save(messageDto)).thenReturn(messageDto);

    MessageDto result = messageController.saveUser(messageDto);

    assertEquals(messageDto, result);
    verify(messageService, times(1)).save(messageDto);
  }


  @Test
  void updateMessage() {
    Long messageId = 1L;
    MessageDto messageDto = new MessageDto();
    messageDto.setId(messageId);
    messageDto.setMessage("Updated message");

    when(messageService.update(messageId, messageDto)).thenReturn(messageDto);

    MessageDto result = messageController.update(messageId, messageDto);

    assertEquals(messageDto, result);

    verify(messageService, times(1)).update(messageId, messageDto);
  }


  @Test
  void deleteMessageServiceDelete() {
    messageController.delete(1L);
    verify(messageService, times(1)).delete(1L);
  }


  @Test
  void deleteThrowNotFound() {
    doThrow(new NotFoundException("Message with id 1 not found")).when(messageService).delete(1L);

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> messageController.delete(1L));

    assertEquals("Message with id 1 not found", exception.getMessage());
    verify(messageService, times(1)).delete(1L);
  }


  @Test
  void saveThrowNotFoundUser() {
    // Мокируем поведение сервиса при сохранении
    when(messageService.save(messageDto)).thenThrow(
        new NotFoundException("User with id 1 not found."));

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> messageController.saveUser(messageDto));

    assertEquals("User with id 1 not found.", exception.getMessage());
    verify(messageService, times(1)).save(messageDto);
  }

}
