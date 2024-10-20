package com.springdemo.mapper;

import com.springdemo.dto.UserDto;
import com.springdemo.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "chats", target = "chats")
  UserDto toDTO(User user);


  @Mapping(source = "username", target = "username")
  @Mapping(source = "chats", target = "chats")
  User toEntity(UserDto userDTO);

  List<UserDto> toDTOList(List<User> users);

  List<User> toEntityList(List<UserDto> userDTOs);

}
