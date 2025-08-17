package com.tweetapp.tweetappbackend.service;

import com.tweetapp.tweetappbackend.dto.AuthenticationResponse;
import com.tweetapp.tweetappbackend.dto.LoginRequest;
import com.tweetapp.tweetappbackend.dto.RefreshTokenRequest;
import com.tweetapp.tweetappbackend.dto.RegisterRequest;
import com.tweetapp.tweetappbackend.exception.TweetAppException;
import com.tweetapp.tweetappbackend.exception.UserNotFoundException;
import com.tweetapp.tweetappbackend.model.NotificationEmail;
import com.tweetapp.tweetappbackend.model.Tweet;
import com.tweetapp.tweetappbackend.model.User;
import com.tweetapp.tweetappbackend.model.VerificationToken;
import com.tweetapp.tweetappbackend.repository.TweetRepository;
import com.tweetapp.tweetappbackend.repository.UserRepository;
import com.tweetapp.tweetappbackend.repository.VerificationTokenRepository;
import com.tweetapp.tweetappbackend.security.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private  MailService mailService;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtProvider jwtProvider;
    @Autowired
    private  RefreshTokenService refreshTokenService;
    @Autowired
    private TweetRepository tweetRepository;

    private final Logger logger= LoggerFactory.getLogger(UserService.class);

    public boolean register(RegisterRequest registerRequest) {
        User user = new User();
        //user.setUserId(registerRequest.getUserId());
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setContactNumber(registerRequest.getContactNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(new Date());
        user.setEnabled(false);
        userRepository.save(user);
        logger.info("User saved successfully");
        String token = generateVerificationToken(user);
       mailService.sendMail(new NotificationEmail("Please Activate your Account",
               user.getEmail(),"Thank you for signing you to Tweet App, "+
               "please click on the below url to activate your account : "+
               "http://localhost:8080/api/v1.0/tweets/accountVerification/"+token));
       return true;
    }

    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        logger.info("token saved successfully");
        return token;
    }

    public boolean verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(()-> new TweetAppException("Invalid token")));
        logger.info("User verified successfully");
        return true;
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUserName();
        try {
            User user = getUserByUserName(username);
            user.setEnabled(true);
            userRepository.save(user);
            logger.info("User saved successfully");
        }catch(Exception exception){
            throw new UserNotFoundException("User Not Found");
        }
    }

    public List<User> getAllUsers(){
        try {
            logger.info("getting users");
            return userRepository.findAll();
        }catch(Exception exception){
            throw new UserNotFoundException("User Not Found");
        }
    }

    public User getUserByUserName(String username){
        try {
            logger.info("getting users by username");
            return userRepository.findByUserName(username);
        }catch(Exception exception){
            throw new UserNotFoundException("User Not Found");
        }
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        logger.info("Authenticating users");
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();

    }

    public Map<String,String> forgotPassword(String username){
        String status= "Success";
        Map<String,String> map = new HashMap<>();
        try{
            String password = UUID.randomUUID().toString();
            String newPassword = passwordEncoder.encode(password);
            User user = getUserByUserName(username);
            user.setPassword(newPassword);
            List<Tweet> tweets = tweetRepository.findByUserUserName(user.getUserName());
            for(Tweet tweet : tweets) {
                tweet.setUser(user);
                tweetRepository.save(tweet);
            }
            userRepository.save(user);
            logger.info("user saved successfully");
            map.put("newPassword",password);
            map.put("resetStatus",status);
        }catch(Exception exception){
            throw new UserNotFoundException("User Not Found");
        }
        return map;
    }

    public Map<String,String> resetPassword(LoginRequest loginRequest){
        String status= "Success";
        Map<String,String> map = new HashMap<>();
        try {
            String password = loginRequest.getPassword();
            String newPassword = passwordEncoder.encode(password);
            User user = getUserByUserName(loginRequest.getUsername());
            user.setPassword(newPassword);
            List<Tweet> tweets = tweetRepository.findByUserUserName(user.getUserName());
            for(Tweet tweet : tweets) {
                tweet.setUser(user);
                tweetRepository.save(tweet);
            }
            userRepository.save(user);
            logger.info("user saved successfully");
            map.put("newPassword", password);
            map.put("resetStatus", status);
        }catch(Exception exception){
            throw new UserNotFoundException("User Not Found");
        }
        return map;
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        logger.info("generated refresh token successfully");
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
