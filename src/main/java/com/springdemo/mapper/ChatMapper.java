package com.springdemo.mapper;

import com.springdemo.dto.ChatDto;
import com.springdemo.dto.MessageDto;
import com.springdemo.entity.Chat;
import com.springdemo.entity.Message;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "chatName", target = "chatName")
  @Mapping(target = "participants", source = "participants")
  @Mapping(target = "messages", source = "messages")
  ChatDto toDTO(Chat chat);

  @Mapping(source = "chatName", target = "chatName")
  @Mapping(target = "participants", source = "participants")
  Chat toEntity(ChatDto chatDto);

  List<ChatDto> toDTOList(List<Chat> chats);

  List<Chat> toEntityList(List<ChatDto> chatDTOs);

  @Mapping(source = "senderId", target = "sender.id")
  @Mapping(source = "chatId", target = "chat.id")
  Message toEntity(MessageDto messageDto);

  @Mapping(source = "sender.id", target = "senderId")
  @Mapping(source = "chat.id", target = "chatId")
  MessageDto toDTO(Message message);
}
