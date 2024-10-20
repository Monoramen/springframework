package com.springdemo.service;


import com.springdemo.dto.UserDto;
import com.springdemo.entity.User;
import com.springdemo.exception.NotFoundException;
import com.springdemo.mapper.UserMapper;
import com.springdemo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMapper userMapper;

  public List<UserDto> findAll() {
    List<User> userList = userRepository.findAll();
    return userList.stream()
        .map(userMapper::toDTO)
        .collect(Collectors.toList());
  }

  public UserDto findById(Long id) {
    Optional<User> user = Optional.ofNullable(userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User with id " + id + " not found")));
    System.out.println(user);
    return userMapper.toDTO(user.get());
  }

  public UserDto save(UserDto userDto) {
    User user = userMapper.toEntity(userDto);
    return userMapper.toDTO(userRepository.save(user));
  }

  public void delete(Long id) {
    Optional<User> user = Optional.ofNullable(userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User with id " + id + " not found")));
    userRepository.delete(user.get());
  }

}
