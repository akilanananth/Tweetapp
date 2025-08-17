package com.tweetapp.tweetappbackend.service;

import com.tweetapp.tweetappbackend.dto.LoginRequest;
import com.tweetapp.tweetappbackend.dto.RegisterRequest;
import com.tweetapp.tweetappbackend.exception.TweetAppException;
import com.tweetapp.tweetappbackend.model.NotificationEmail;
import com.tweetapp.tweetappbackend.model.User;
import com.tweetapp.tweetappbackend.model.VerificationToken;
import com.tweetapp.tweetappbackend.repository.UserRepository;
import com.tweetapp.tweetappbackend.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    VerificationTokenRepository verificationTokenRepository;

    @Mock
    MailService mailService;

    @Test
    void register() {
        User user = new User("1","TestUser1","test1","testuser1@gmail.com","87654321",new Date(),false);
        System.out.println(user);
        when(userRepository.save(user)).thenReturn(user);
        RegisterRequest registerRequest = new RegisterRequest("TestUser1","testuser1@gmail,com","test1","87654321");
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("32678ryiugehjwk");
        System.out.println(registerRequest);
        when(verificationTokenRepository.save(any(VerificationToken.class))).thenReturn(new VerificationToken());
        mailService.sendMail(any(NotificationEmail.class));
        assertTrue(userService.register(registerRequest));
    }

    @Test
    void getAllUsers() {
        User user = new User("1","TestUser1","test1","testuser1@gmail.com","87654321",new Date(),false);
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> savedUser=userService.getAllUsers();
        assertEquals(savedUser,users);
    }

    @Test
    void getUserByUserName() {
        User user = new User("1","TestUser1","test1","testuser1@gmail.com","87654321",new Date(),false);
        when(userRepository.findByUserName("TestUser1")).thenReturn(user);
        User savedUser=userService.getUserByUserName("TestUser1");
        assertEquals(savedUser,user);
    }

    @Test
    void forgotPassword() {
        User user = new User("1","TestUser1","test1","testuser1@gmail.com","87654321",new Date(),false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByUserName("TestUser1")).thenReturn(user);
        Map<String,String> map = new HashMap<>();
        map.put("newPassword","new_test1");
        map.put("resetStatus","Success");
        Map<String,String> result = userService.forgotPassword("TestUser1");
        assertEquals(map.get(1),result.get(1));
    }

    @Test
    void resetPassword() {
        User user = new User("1","TestUser1","test1","testuser1@gmail.com","87654321",new Date(),false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        LoginRequest loginRequest = new LoginRequest("TestUser1","new_test1");
        when(userRepository.findByUserName("TestUser1")).thenReturn(user);
        Map<String,String> map = new HashMap<>();
        map.put("newPassword","new_test1");
        map.put("resetStatus","Success");
        Map<String,String> result = userService.resetPassword(loginRequest);
        assertEquals(map,result);
    }
}