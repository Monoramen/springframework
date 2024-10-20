package com.springdemo.controller;

import com.springdemo.dto.UserDto;
import com.springdemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  UserService userService;


  @GetMapping("/findAll")
  public List<UserDto> findAllUsers() {
    List<UserDto> users = userService.findAll();
    return users;
  }

  @GetMapping(value = "/{id}")
  public UserDto findById(@PathVariable("id") Long id) {
    logger.info("Finding user with id: {}", id);
    return userService.findById(id);
  }

  @PostMapping("/addUser")
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto addUser(@RequestBody UserDto userDto) {
    return userService.save(userDto);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
    UserDto userForUpdate = userService.findById(id);
    userDto.setId(userForUpdate.getId());
    userService.save(userDto);
  }


  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void delete(@PathVariable("id") Long id) {
    userService.delete(id);
  }
}
