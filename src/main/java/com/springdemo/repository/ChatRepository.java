package com.springdemo.repository;


import com.springdemo.entity.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

  @EntityGraph(attributePaths = {"participants"})
  List<Chat> findAll();

  @EntityGraph(attributePaths = {"participants"})
  Optional<Chat> findById(Long id);
}
