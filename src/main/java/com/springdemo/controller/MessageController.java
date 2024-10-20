package com.springdemo.controller;

import com.springdemo.dto.MessageDto;
import com.springdemo.service.MessageService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/messages")
public class MessageController {

  private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

  @Autowired
  MessageService messageService;

  @GetMapping("/findAll")
  public List<MessageDto> findAll() {
    return messageService.findAll();
  }

  @PostMapping("/addMessage")
  public MessageDto saveUser(@RequestBody MessageDto messageDto) {
    return messageService.save(messageDto);
  }

  @GetMapping(value = "/{id}")
  public MessageDto findById(@PathVariable("id") Long id) {
    return messageService.findById(id);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MessageDto update(@PathVariable("id") Long id, @RequestBody MessageDto messageDto) {
    return messageService.update(id, messageDto);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void delete(@PathVariable("id") Long id) {
    messageService.delete(id);
  }

}
