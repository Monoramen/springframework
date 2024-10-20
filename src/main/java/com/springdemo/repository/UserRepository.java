package com.springdemo.repository;

import com.springdemo.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = {"chats"})
  Optional<User> findById(Long id);

  @EntityGraph(attributePaths = {"chats"})
  List<User> findAll();
}
