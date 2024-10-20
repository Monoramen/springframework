package com.springdemo.repository;

import com.springdemo.entity.Chat;
import com.springdemo.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        chatRepository.deleteAll();
    }

    @Test
    void addUserTest() {
        User user = new User("UserName", "pass");
        Chat chat = new Chat("ChatName");
        List<User> participants = new ArrayList<>();
        participants.add(user);
        List<Chat> chats = new ArrayList<>();
        chats.add(chat);
        chat.setMessages(new ArrayList<>());
        chat.setParticipants(participants);
        user.setChats(chats);

        chatRepository.save(chat);
        userRepository.save(user);
        User retrievedUser = userRepository.getById(user.getId());

        assertNotNull(retrievedUser);
        assertEquals("UserName", retrievedUser.getUsername());
        assertEquals("pass", retrievedUser.getPassword());
        assertNotNull(userRepository.getById(1L));
    }


    @Test
    void notSuccessAddUserTest() {
        User user1 = new User("UserName", "pass");
        userRepository.save(user1);

        User user2 = new User("UserName", "anotherPass");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
        });

    }

    @Test
    void updateUserTest() {
        User user = new User("UserName", "pass");
        userRepository.save(user);

        User updateUser = userRepository.getById(user.getId());
        updateUser.setUsername("UpdateUsername");
        updateUser.setPassword("UpdatePass");

        userRepository.save(updateUser);

        User checkUpdateUserData = userRepository.getById(user.getId());
        assertEquals("UpdateUsername", checkUpdateUserData.getUsername());
        assertEquals("UpdatePass", checkUpdateUserData.getPassword());
    }


    @Test
    void notSuccessUpdateUserTest() {
        User user1 = new User("UserName", "pass");
        userRepository.save(user1);
        User user2 = new User("UserName", "UpdatePass");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
        });
    }


    @Test
    void deleteUserTest(){
        User user = new User("UserName", "pass");
        userRepository.save(user);
        assertNotNull(userRepository.getById(user.getId()));

        userRepository.delete(user);
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void notSuccessdeleteUserTest(){
        User user = new User("UserName", "pass");
        userRepository.save(user);
        userRepository.delete(user);
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    void selectUserTest(){
        User user = new User("UserName", "pass");
        userRepository.save(user);

        User expectedUser = userRepository.getById(user.getId());
        assertEquals(expectedUser, userRepository.getById(user.getId()));
    }
    @Test
    void notSuccessselectUserTest(){
        User user = new User("UserName", "pass");
        userRepository.save(user);

        User expectedUser = userRepository.getById(user.getId());
        assertNotEquals(expectedUser, userRepository.findById(2L));

        System.out.println(userRepository.findById(2L));
    }

}
