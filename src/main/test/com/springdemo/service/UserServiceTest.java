package com.springdemo.service;

import com.springdemo.dto.ChatInfoDto;
import com.springdemo.dto.UserDto;
import com.springdemo.entity.Chat;
import com.springdemo.entity.User;
import com.springdemo.exception.NotFoundException;
import com.springdemo.mapper.UserMapper;
import com.springdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void findAllUsers() {
    User user1 = new User("User1", "pass1");
    user1.setId(1L);

    User user2 = new User("User2", "pass2");
    user2.setId(2L);

    Chat chat = new Chat("Chat1");
    chat.setId(1L);

    user1.getChats().add(chat);
    user2.getChats().add(chat);


    when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

    ChatInfoDto chatInfoDto = new ChatInfoDto();
    chatInfoDto.setChatName(chat.getChatName());
    chatInfoDto.setId(chat.getId());


    UserDto userDto1 = new UserDto();
    userDto1.setId(user1.getId());
    userDto1.setUsername(user1.getUsername());
    userDto1.getChats().add(chatInfoDto);

    UserDto userDto2 = new UserDto();
    userDto2.setId(user2.getId());
    userDto2.setUsername(user2.getUsername());
    userDto2.getChats().add(chatInfoDto);

    when(userMapper.toDTO(user1)).thenReturn(userDto1);
    when(userMapper.toDTO(user2)).thenReturn(userDto2);

    List<UserDto> userDtos = userService.findAll();

    assertEquals(userDtos.get(0), userDto1);
    assertEquals(userDtos.get(1), userDto2);
    assertEquals(2, userDtos.size());
    verify(userRepository, times(1)).findAll();
    verify(userMapper, times(2)).toDTO(any(User.class));
  }

  @Test
  void findByIdUser() {
    User user = new User("User1", "pass1");
    user.setId(1L);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());

    when(userMapper.toDTO(user)).thenReturn(userDto);

    UserDto foundUser = userService.findById(1L);

    assertEquals(userDto.getId(),foundUser.getId() );
    assertEquals(userDto.getUsername(),foundUser.getUsername());

    verify(userRepository, times(1)).findById(1L);
    verify(userMapper, times(1)).toDTO(user);
  }

  @Test
  void findByIdThrowNotFoundException() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> userService.findById(1L));
    assertEquals("User with id 1 not found", exception.getMessage());
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  void saveUserDto() {
    User user = new User("User1", "pass1");

    UserDto userDto = new UserDto();
    userDto.setUsername(user.getUsername());
    userDto.setPassword(user.getPassword());

    when(userMapper.toEntity(userDto)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDto);

    UserDto savedUser = userService.save(userDto);


    assertNotNull(savedUser);
    assertEquals(savedUser.getUsername(), userDto.getUsername());
    assertEquals(savedUser.getPassword(), userDto.getPassword());

    verify(userRepository, times(1)).save(user);
    verify(userMapper, times(1)).toEntity(userDto);
    verify(userMapper, times(1)).toDTO(user);
  }

  @Test
  void deleteUser() {
    User user = new User("User1", "pass1");
    user.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    userService.delete(1L);

    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  void deleteThrowNotFoundException() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> userService.delete(1L));
    assertEquals("User with id 1 not found", exception.getMessage());
    verify(userRepository, times(1)).findById(1L);
  }
}
