package com.springdemo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springdemo.dto.ChatDto;
import com.springdemo.dto.CreateChatDto;
import com.springdemo.dto.UserInfoDto;
import com.springdemo.entity.Chat;
import com.springdemo.entity.User;
import com.springdemo.exception.NotFoundException;
import com.springdemo.mapper.ChatMapper;
import com.springdemo.mapper.UserMapper;
import com.springdemo.repository.ChatRepository;
import com.springdemo.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ChatServiceTest {

  @Mock
  private ChatRepository chatRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChatMapper chatMapper;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private ChatService chatService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void findAllReturnListOfChats() {
    Chat chat1 = new Chat();
    chat1.setId(1L);
    chat1.setChatName("chat1");
    Chat chat2 = new Chat();
    chat2.setId(2L);
    chat2.setChatName("chat2");

    ChatDto chatDto1 =  new ChatDto();
    chatDto1.setId(1L);
    chatDto1.setChatName("chat1");
    ChatDto chatDto2 =  new ChatDto();
    chatDto2.setId(1L);
    chatDto2.setChatName("chat1");



    when(chatRepository.findAll()).thenReturn(Arrays.asList(chat1, chat2));
    when(chatMapper.toDTO(chat1)).thenReturn(chatDto1);
    when(chatMapper.toDTO(chat2)).thenReturn(chatDto2);

    List<ChatDto> chatDtos = chatService.findAll();

    assertEquals(2, chatDtos.size());
    verify(chatRepository, times(1)).findAll();
    verify(chatMapper, times(2)).toDTO(any(Chat.class));
  }

  @Test
  void findByIdReturnChat() {
    Chat chat = new Chat();
    chat.setId(1L);
    chat.setChatName("Chat");
    User user =  new User("user","pass");
    user.addChat(chat);
    user.setId(1L);

    List<UserInfoDto> participants = new ArrayList<>();
    UserInfoDto userInfoDto =  new UserInfoDto();
    userInfoDto.setId(user.getId());
    userInfoDto.setUsername(user.getUsername());

    participants.add(userInfoDto);

    ChatDto chatDtoReturn = new ChatDto(1L, "Chat", participants);
    when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
    when(chatMapper.toDTO(chat)).thenReturn(chatDtoReturn);

    ChatDto chatDto = chatService.findById(1L);

    assertNotNull(chatDto);
    assertEquals(1L, chatDto.getId());
    assertEquals("Chat", chatDto.getChatName());
    assertEquals(participants, chatDto.getParticipants());

  }

  @Test
  void findByIdThrowNotFoundException() {
    when(chatRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> chatService.findById(1L));
    assertEquals("Chat with id 1 not found", exception.getMessage());
    verify(chatRepository, times(1)).findById(1L);
  }

  @Test
  void saveChatAndReturnDto() {
    // Arrange
    CreateChatDto createChatDto = new CreateChatDto();
    createChatDto.setChatName("Test Chat");
    createChatDto.setParticipants(Arrays.asList(1L, 2L));

    Chat chat = new Chat();
    chat.setChatName(createChatDto.getChatName());
    chat.setId(1L);

    ChatDto chatDto = new ChatDto();
    chatDto.setId(1L);
    chatDto.setChatName("Test Chat");

    User user1 = new User("User1", "pass");
    user1.setId(1L);
    User user2 = new User("User2", "pass");
    user2.setId(2L);

    List<User> userList = Arrays.asList(user1, user2);

    when(chatRepository.save(any(Chat.class))).thenReturn(chat);
    when(userRepository.findAllById(createChatDto.getParticipants())).thenReturn(userList);
    when(chatMapper.toDTO(any(Chat.class))).thenReturn(chatDto);

    ChatDto savedChatDto = chatService.save(createChatDto);

    assertNotNull(savedChatDto);
    assertEquals("Test Chat", savedChatDto.getChatName());
    verify(chatRepository, times(1)).save(any(Chat.class));
    verify(userRepository, times(1)).findAllById(createChatDto.getParticipants());
    verify(userRepository, times(1)).saveAll(userList);
    verify(chatMapper, times(1)).toDTO(any(Chat.class));
  }

  @Test
  void updateChat() {
    ChatDto chatDto = new ChatDto();
    chatDto.setId(1L);
    chatDto.setChatName("Updated Chat");

    Chat existingChat = new Chat();
    existingChat.setId(1L);
    existingChat.setChatName("Old Chat");

    Chat updatedChat = new Chat();
    updatedChat.setId(1L);
    updatedChat.setChatName("Updated Chat");

    when(chatRepository.findById(1L)).thenReturn(Optional.of(existingChat));
    when(chatRepository.save(any(Chat.class))).thenReturn(updatedChat);
    when(chatMapper.toDTO(updatedChat)).thenReturn(chatDto);

    ChatDto result = chatService.update(chatDto);

    assertNotNull(result);
    assertEquals("Updated Chat", result.getChatName());
    verify(chatRepository, times(1)).findById(1L);
    verify(chatRepository, times(1)).save(any(Chat.class));
    verify(chatMapper, times(1)).toDTO(any(Chat.class));
  }

  @Test
  void updateThrowNotFoundException() {
    ChatDto chatDto = new ChatDto();
    chatDto.setId(1L);
    chatDto.setChatName("Nonexistent Chat");

    when(chatRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> chatService.update(chatDto));
    assertEquals("Chat with id 1 not found", exception.getMessage());
    verify(chatRepository, times(1)).findById(1L);
    verify(chatRepository, times(0)).save(any(Chat.class));
  }
  @Test
  void deleteChat() {
    Chat chat = new Chat("Chat1");
    chat.setId(1L);

    User user1 = new User("user", "qwerty");
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);
    chat.setParticipants(new ArrayList<>(Arrays.asList(user1, user2)));

    when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

    chatService.delete(1L);

    verify(chatRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).saveAll(anyList());
    verify(chatRepository, times(1)).delete(chat);

    when(chatRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> chatService.delete(1L));
    assertEquals("Chat with id 1 not found", exception.getMessage());

  }

  @Test
  void deleteThrowNotFoundException() {
    when(chatRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> chatService.delete(1L));
    assertEquals("Chat with id 1 not found", exception.getMessage());
    verify(chatRepository, times(1)).findById(1L);
  }

}