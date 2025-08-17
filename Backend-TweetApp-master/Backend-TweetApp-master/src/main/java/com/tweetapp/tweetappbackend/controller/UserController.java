package com.tweetapp.tweetappbackend.controller;

import com.tweetapp.tweetappbackend.dto.AuthenticationResponse;
import com.tweetapp.tweetappbackend.dto.LoginRequest;
import com.tweetapp.tweetappbackend.dto.RefreshTokenRequest;
import com.tweetapp.tweetappbackend.dto.RegisterRequest;
import com.tweetapp.tweetappbackend.model.User;
import com.tweetapp.tweetappbackend.service.UserService;
import com.tweetapp.tweetappbackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin
public class UserController {
    @Autowired
    private  UserService userService;
    @Autowired
    private  RefreshTokenService refreshTokenService;

    private Logger logger= LoggerFactory.getLogger(UserController.class);


    @PostMapping("/register")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        userService.register(registerRequest);
        logger.info("User registered");
        return new ResponseEntity<>("User Registration Successful",
                HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest){
        logger.info("Logging in User");
        return new ResponseEntity<>(userService.login(loginRequest),HttpStatus.OK);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers(){
        logger.info("Getting Users");
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public  ResponseEntity<String> verifyAccount(@PathVariable String token){
        userService.verifyAccount(token);
        logger.info("Account verified");
        return new ResponseEntity<>("Account Activated Successfully!!!",HttpStatus.OK);
    }

    @GetMapping("{username}/forgot")
    public ResponseEntity<Map<String,String>> forgotPassword(@PathVariable String username){
        logger.info("Generated new password");
        return new ResponseEntity<>(new HashMap<>(userService.forgotPassword(username)),HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String,String>> resetPassword(@RequestBody LoginRequest loginRequest){
        logger.info("Reset Password complete");
        return new ResponseEntity<>(new HashMap<>(userService.resetPassword(loginRequest)),HttpStatus.OK);
    }

    @GetMapping("/user/search/{username}")
    public ResponseEntity<User> searchUser(@PathVariable String username){
        logger.info("Getting User by username");
        return new ResponseEntity<>(userService.getUserByUserName(username),HttpStatus.OK);
    }


    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        logger.info("generating refresh token");
        return userService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        logger.info("Signed out User");
        return new ResponseEntity<>("Logged out Successfully!!!",HttpStatus.OK);
    }
}
