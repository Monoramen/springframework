package com.springdemo.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.springdemo.entity.Chat;
import com.springdemo.entity.Message;
import com.springdemo.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DBTestConfig.class}) // Указываем
@EnableJpaRepositories
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
        chatRepository.deleteAll();
    }

    @Test
    void addMessageTest() {
        User user = new User("username", "pass");
        User sender = userRepository.save(user);

        Chat chat = new Chat("chat");
        sender.addChat(chat);

        Chat savedchat = chatRepository.save(chat);

        Message message = new Message("Test Message", savedchat, sender);
        Message saved = messageRepository.save(message);

        // Проверка на успешное сохранение
        assertNotNull(saved.getId());
        assertEquals("Test Message", saved.getMessage());
        assertEquals(sender.getId(), saved.getSender().getId());
        assertEquals(chat.getId(), saved.getChat().getId());
    }

    @Test
    void notSuccessAddMessageTest() {
        Message message = new Message("Invalid Message", null, null);
        Exception exception = null;
        try {
           Message saved = messageRepository.save(message);
        } catch (Exception e) {
            exception = e;
        }

        assertNotNull(exception);
    }

    @Test
    void updateMessageTest() {
        User user = new User("username", "pass");
        User sender = userRepository.save(user);

        Chat chat = new Chat("chat");
        Chat savedChat = chatRepository.save(chat);

        Message message = new Message("Original Message", savedChat, sender);
        Message savedMessage = messageRepository.save(message);

        // Обновляем сообщение
        savedMessage.setMessage("Updated Message");
        Message updatedMessage = messageRepository.save(savedMessage);

        // Проверка на успешное обновление
        assertEquals("Updated Message", updatedMessage.getMessage());
    }

    @Test
    void notSuccessUpdateMessageTest() {
        // Попытка обновить несуществующее сообщение
        Message message = new Message();
        message.setId(999L); // Устанавливаем несуществующий ID
        Exception exception = null;

        try {
            messageRepository.save(message);
        } catch (Exception e) {
            exception = e; // Ловим исключение
        }

        // Проверка, что было выброшено исключение
        assertNotNull(exception);
    }

    @Test
    void deleteMessageTest() {
        User user = new User("username", "pass");
        User sender = userRepository.save(user);

        Chat chat = new Chat("chat");
        Chat savedChat = chatRepository.save(chat);

        Message message = new Message("Message to delete", savedChat, sender);
        Message savedMessage = messageRepository.save(message);


        messageRepository.delete(savedMessage);

        assertEquals(0, messageRepository.count());
    }

    @Test
    void notSuccessDeleteMessageTest() {
        Long nonExistentId = 999L;
        Optional<Message> messageOptional = messageRepository.findById(nonExistentId);

        assertEquals(Optional.empty(), messageOptional);

        Message message = new Message();
        message.setId(nonExistentId);

        messageRepository.delete(message);

        Optional<Message> afterDelete = messageRepository.findById(nonExistentId);
        assertEquals(Optional.empty(), afterDelete); // Сообщение все еще не должно существовать
    }


    @Test
    void selectMessageTest() {
        User user = new User("username", "pass");
        User sender = userRepository.save(user);

        Chat chat = new Chat("chat");
        Chat savedChat = chatRepository.save(chat);

        Message message = new Message("Test Message", savedChat, sender);
        Message savedMessage = messageRepository.save(message);

        // Поиск сообщения по ID
        Message foundMessage = messageRepository.findById(savedMessage.getId()).orElse(null);

        // Проверка на успешное получение
        assertNotNull(foundMessage);
        assertEquals(savedMessage.getId(), foundMessage.getId());
    }

    @Test
    void notSuccessSelectMessageTest() {
        // Попытка получить сообщение с несуществующим ID
        Message foundMessage = messageRepository.findById(999L).orElse(null);

        // Проверка, что сообщение не найдено
        assertEquals(null, foundMessage);
    }

}
