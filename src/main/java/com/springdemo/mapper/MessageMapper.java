package com.springdemo.mapper;

import com.springdemo.dto.MessageDto;
import com.springdemo.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  @Mapping(source = "sender.id", target = "senderId")  // Маппим идентификатор отправителя
  @Mapping(source = "chat.id", target = "chatId")      // Маппим идентификатор чата
  MessageDto toDTO(Message message);

  @Mapping(source = "senderId", target = "sender.id")  // Устанавливаем отправителя из ID
  @Mapping(source = "chatId", target = "chat.id")      // Устанавливаем чат из ID
  Message toEntity(MessageDto messageDto);

  List<MessageDto> toDTOList(List<Message> messages);
  List<Message> toEntityList(List<MessageDto> messageDTOs);
}
