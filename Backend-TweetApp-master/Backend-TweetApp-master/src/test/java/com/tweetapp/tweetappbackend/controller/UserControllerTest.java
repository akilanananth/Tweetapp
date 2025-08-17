package com.tweetapp.tweetappbackend.controller;

import com.tweetapp.tweetappbackend.dto.AuthenticationResponse;
import com.tweetapp.tweetappbackend.dto.LoginRequest;
import com.tweetapp.tweetappbackend.dto.RefreshTokenRequest;
import com.tweetapp.tweetappbackend.dto.RegisterRequest;
import com.tweetapp.tweetappbackend.model.User;
import com.tweetapp.tweetappbackend.service.RefreshTokenService;
import com.tweetapp.tweetappbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    RefreshTokenService refreshTokenService;

    @InjectMocks
    UserController userController;

    @Test
    void testSignup() {
        userService.register(any(RegisterRequest.class));
        RegisterRequest register = new RegisterRequest("TestUser","testuser@gmail,com","test","12348765");
        ResponseEntity<String> responseEntity = userController.signup(register);
        assertEquals(responseEntity.getStatusCodeValue(),200);
    }

    @Test
    void testLogin() {
        userService.login(any(LoginRequest.class));
        LoginRequest loginRequest=new LoginRequest("TestUser","test");
        ResponseEntity<AuthenticationResponse> responseEntity = userController.login(loginRequest);
        assertEquals(responseEntity.getStatusCodeValue(),200);
    }


    @Test
    void testGetAllUsers() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        User user2 = new User("2","TestUser2","test2","testuser2@gmail.com","12345678",new Date(),true);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userService.getAllUsers()).thenReturn(users);
        assertEquals(userController.getAllUsers().getBody(),users);
        assertEquals(userController.getAllUsers().getStatusCodeValue(),200);
    }

    @Test
    void testForgotPassword() {
        Map<String,String> map = new HashMap<>();
        map.put("newPassword","password");
        map.put("resetStatus","Success");
        when(userService.forgotPassword("TestUser")).thenReturn(map);
        assertEquals(userController.forgotPassword("TestUser").getBody(),map);
    }

    @Test
    void testResetPassword() {
        Map<String,String> map = new HashMap<>();
        map.put("newPassword","test");
        map.put("resetStatus","Success");
        when(userService.resetPassword(any(LoginRequest.class))).thenReturn(map);
        LoginRequest loginRequest=new LoginRequest("TestUser","test");
        assertEquals(userController.resetPassword(loginRequest).getBody(),map);
    }


    @Test
    void testSearchUser() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        when(userService.getUserByUserName("TestUser1")).thenReturn(user1);
        assertEquals(userController.searchUser("TestUser1").getBody(),user1);
        assertEquals(userController.searchUser("TestUser1").getStatusCodeValue(),200);
    }


    @Test
    void verifyAccount() {
        userService.verifyAccount("ygrfbhj5467382");
        assertEquals(userController.verifyAccount("ygrfbhj5467382").getStatusCodeValue(),200);
    }


    @Test
    void refreshTokens() {
        AuthenticationResponse authenticationResponse=new AuthenticationResponse("tfduyqwjxk189","e5467tw8yqshjb",Instant.now(),"TestUser1");
        when(userService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(authenticationResponse);
        RefreshTokenRequest refreshTokenRequest=new RefreshTokenRequest("e5467tw8yqshjb","TestUser1");
        assertEquals(userController.refreshTokens(refreshTokenRequest),authenticationResponse);
    }

    @Test
    void logout() {
        refreshTokenService.deleteRefreshToken("e5467tw8yqshjb");
        RefreshTokenRequest refreshTokenRequest=new RefreshTokenRequest("e5467tw8yqshjb","TestUser1");
        assertEquals(userController.logout(refreshTokenRequest).getStatusCodeValue(),200);
    }
}