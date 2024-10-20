package com.springdemo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springdemo.dto.ChatDto;
import com.springdemo.dto.CreateChatDto;
import com.springdemo.exception.NotFoundException;
import com.springdemo.service.ChatService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChatControllerTest {

  @InjectMocks
  private ChatController chatController;

  @Mock
  private ChatService chatService;

  private ChatDto chatDto;

  private CreateChatDto createChatDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    chatDto = new ChatDto();
    chatDto.setId(1L);
    chatDto.setChatName("Test Chat");

    createChatDto = new CreateChatDto();
    createChatDto.setChatName("New Chat");
  }

  @Test
  void findAllUsers_ShouldReturnListOfChats() {
    when(chatService.findAll()).thenReturn(Collections.singletonList(chatDto));

    List<ChatDto> result = chatController.findAllUsers();

    assertEquals(1, result.size());
    assertEquals(chatDto, result.get(0));
    verify(chatService, times(1)).findAll();
  }

  @Test
  void findById_ShouldReturnChat_WhenExists() {
    when(chatService.findById(1L)).thenReturn(chatDto);

    ChatDto result = chatController.findById(1L);

    assertEquals(chatDto, result);
    verify(chatService, times(1)).findById(1L);
  }

  @Test
  void findById_ShouldThrowNotFound_WhenChatDoesNotExist() {
    when(chatService.findById(1L)).thenThrow(new NotFoundException("Chat with id 1 not found"));

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> chatController.findById(1L));

    assertEquals("Chat with id 1 not found", exception.getMessage());
    verify(chatService, times(1)).findById(1L);
  }

  @Test
  void addChat_ShouldReturnSavedChat() {
    when(chatService.save(createChatDto)).thenReturn(chatDto);

    ChatDto result = chatController.add(createChatDto);

    assertEquals(chatDto, result);
    verify(chatService, times(1)).save(createChatDto);
  }

  @Test
  void update_ShouldUpdateChat_WhenChatExists() {
    when(chatService.findById(1L)).thenReturn(chatDto);
    when(chatService.update(chatDto)).thenReturn(chatDto);

    chatController.update(1L, chatDto);

    verify(chatService, times(1)).findById(1L);
    verify(chatService, times(1)).update(chatDto);
  }

  @Test
  void update_ShouldThrowNotFound_WhenChatDoesNotExist() {
    when(chatService.findById(1L)).thenThrow(new NotFoundException("Chat with id 1 not found"));

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> chatController.update(1L, chatDto));

    assertEquals("Chat with id 1 not found", exception.getMessage());
    verify(chatService, times(1)).findById(1L);
  }

  @Test
  void delete_ShouldCallChatServiceDelete() {
    chatController.delete(1L);

    verify(chatService, times(1)).delete(1L);
  }
}
