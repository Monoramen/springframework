package com.springdemo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springdemo.dto.UserDto;
import com.springdemo.exception.NotFoundException;
import com.springdemo.service.UserService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userService;

  private UserDto userDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userDto = new UserDto();
    userDto.setId(1L);
    userDto.setUsername("testUser");
    userDto.setPassword("testPassword");
  }

  @Test
  void findAllUsers_ShouldReturnListOfUsers() {
    when(userService.findAll()).thenReturn(Collections.singletonList(userDto));

    List<UserDto> result = userController.findAllUsers();

    assertEquals(1, result.size());
    assertEquals(userDto, result.get(0));
    verify(userService, times(1)).findAll();
  }

  @Test
  void findById_ShouldReturnUser_WhenExists() {
    when(userService.findById(1L)).thenReturn(userDto);

    UserDto result = userController.findById(1L);

    assertEquals(userDto, result);
    verify(userService, times(1)).findById(1L);
  }

  @Test
  void findById_ShouldThrowNotFound_WhenUserDoesNotExist() {
    when(userService.findById(1L)).thenThrow(new NotFoundException("User with id 1 not found"));

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> userController.findById(1L));

    assertEquals("User with id 1 not found", exception.getMessage());
    verify(userService, times(1)).findById(1L);
  }

  @Test
  void addUser_ShouldReturnSavedUser() {
    when(userService.save(userDto)).thenReturn(userDto);

    UserDto result = userController.addUser(userDto);

    assertEquals(userDto, result);
    verify(userService, times(1)).save(userDto);
  }

  @Test
  void update_ShouldUpdateUser_WhenUserExists() {
    when(userService.findById(1L)).thenReturn(userDto);
    when(userService.save(userDto)).thenReturn(userDto);

    userController.update(1L, userDto);

    verify(userService, times(1)).findById(1L);
    verify(userService, times(1)).save(userDto);
  }

  @Test
  void update_ShouldThrowNotFound_WhenUserDoesNotExist() {
    when(userService.findById(1L)).thenThrow(new NotFoundException("User with id 1 not found"));

    NotFoundException exception = assertThrows(NotFoundException.class,
        () -> userController.update(1L, userDto));

    assertEquals("User with id 1 not found", exception.getMessage());
    verify(userService, times(1)).findById(1L);
  }

  @Test
  void delete_ShouldCallUserServiceDelete() {
    userController.delete(1L);

    verify(userService, times(1)).delete(1L);
  }
}
