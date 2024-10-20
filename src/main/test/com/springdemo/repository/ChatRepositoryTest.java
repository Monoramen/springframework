package com.springdemo.repository;


import com.springdemo.entity.Chat;
import com.springdemo.entity.Message;
import com.springdemo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DBTestConfig.class}) // Указываем
@EnableJpaRepositories
@Testcontainers
@ActiveProfiles("test")
@Transactional
@Rollback
public class ChatRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        chatRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    void addChatTest() {
        User user =  new User("Alex", "pass");
        userRepository.save(user);
        User user2 =  new User("Alex2", "pass");
        userRepository.save(user2);

        Chat chat = new Chat("ChatName");
        chatRepository.save(chat);

        List<User> participants = new ArrayList<>();
        participants.add(user);
        participants.add(user2);

        userRepository.saveAll(participants);
        chat.setParticipants(participants);

        Message message = new Message("test", chat, user);
        messageRepository.save(message);

        Chat expectedChat  = chatRepository.save(chat);
        chatRepository.findAll();

        Chat foundChat = chatRepository.getById(expectedChat.getId());

        assertEquals("ChatName", foundChat.getChatName());
        assertEquals(foundChat.getMessages(), chatRepository.getById(expectedChat.getId()).getMessages());
        assertEquals(foundChat.getParticipants(), chatRepository.getById(expectedChat.getId()).getParticipants());
    }


    @Test
    void notSuccessAddChatTest() {
        Chat chat = new Chat("EmptyChat");
        chatRepository.save(chat);

    }


    @Test
    void updateChatTest() {
        User user =  new User("Alex", "pass");
        userRepository.save(user);
        User user2 =  new User("Alex2", "pass");
        userRepository.save(user2);

        Chat chat = new Chat("ChatName");
        chatRepository.save(chat);

        List<User> participants = new ArrayList<>();
        participants.add(user);
        participants.add(user2);

        userRepository.saveAll(participants);
        chat.setParticipants(participants);
        chat.setChatName("newName");
        Chat expectedChat  = chatRepository.save(chat);

        Chat foundChat = chatRepository.getById(expectedChat.getId());

        assertEquals("newName", foundChat.getChatName());
    }



    @Test
    void notSuccessUpdateChatTest() {
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            Chat nonExistentChat = new Chat();
            nonExistentChat.setId(999L);
            nonExistentChat.setChatName("NonExistent");
            chatRepository.save(nonExistentChat);
        });
    }



    @Test
    void deleteChatTest() {
        Chat chat = new Chat("ChatName");
        Chat chatShoudDelete = chatRepository.save(chat);
        assertNotNull(chatShoudDelete);

        chatRepository.delete(chatShoudDelete);
        assertFalse(chatRepository.findById(chatShoudDelete.getId()).isPresent());
    }



    @Test
    void notSuccessDeleteChatTest() {
        Chat chat = new Chat("ChatName");
        chatRepository.save(chat);
        chatRepository.delete(chat);
        assertFalse(userRepository.findById(chat.getId()).isPresent());
    }

    @Test
    void selectChatTest() {
        User user = new User("Alex", "pass");
        userRepository.save(user);

        Chat chat = new Chat("SelectMe");
        chat.setParticipants(List.of(user));

        Chat savedChat = chatRepository.save(chat);
        Chat retrievedChat = chatRepository.getById(savedChat.getId());
        assertEquals(savedChat.getId(), retrievedChat.getId());
    }
    @Test
    void notSuccessSelectChatTest() {
        assertFalse(chatRepository.findById(999L).isPresent());
    }
}
