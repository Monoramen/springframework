package com.springdemo.controller;

import com.springdemo.dto.ChatDto;
import com.springdemo.dto.CreateChatDto;
import com.springdemo.service.ChatService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {

  private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

  @Autowired
  ChatService chatService;

  @GetMapping("/findAll")
  public List<ChatDto> findAllUsers() {
    List<ChatDto> chats = chatService.findAll();
    return chats;
  }

  @GetMapping(value = "/{id}")
  public ChatDto findById(@PathVariable("id") Long id) {
    return chatService.findById(id);
  }
  @PostMapping("/addChat")
  @ResponseStatus(HttpStatus.CREATED)
  public ChatDto add(@RequestBody CreateChatDto chatDto) {
    logger.info("controller =", chatDto);
    return chatService.save(chatDto);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@PathVariable("id") Long id, @RequestBody ChatDto chatDto) {
    ChatDto chatForUpdate = chatService.findById(id);
    chatDto.setId(chatForUpdate.getId());
    chatService.update(chatDto);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void delete(@PathVariable("id") Long id) {
    chatService.delete(id);
  }
}
