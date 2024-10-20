package com.springdemo.repository;

import com.springdemo.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  List<Message> findAllByChatId(Long id);
}
