package com.tweetapp.tweetappbackend.service;

import com.tweetapp.tweetappbackend.dto.CommentsDto;
import com.tweetapp.tweetappbackend.dto.TweetRequest;
import com.tweetapp.tweetappbackend.exception.TweetAppException;
import com.tweetapp.tweetappbackend.mapper.TweetMapper;
import com.tweetapp.tweetappbackend.model.Tweet;
import com.tweetapp.tweetappbackend.model.User;
import com.tweetapp.tweetappbackend.repository.TweetRepository;
import com.tweetapp.tweetappbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TweetServiceTest {

    @InjectMocks
    TweetService tweetService;

    @Mock
    UserRepository userRepository;

    @Mock
    TweetRepository tweetRepository;

    @Mock
    TweetMapper tweetMapper;

    @Test
    void saveTweet() {
        User user = new User("1","TestUser1","test1","testuser1@gmail.com","87654321",new Date(),false);
        when(userRepository.findByUserName("TestUser1")).thenReturn(user);
        TweetRequest tweetRequest=new TweetRequest("TestTweet","Testing tweet App");
        List<String> comments = new ArrayList<>();
        Tweet tweet= new Tweet("1","TestTweet","Testing tweet App",0,user,new Date(),comments);
        when(tweetRepository.save(tweetMapper.map(tweetRequest, user))).thenReturn(tweet);
        assertTrue(tweetService.saveTweet("TestUser1",tweetRequest));
    }

    @Test
    void updateTweet() {
        User user = new User("1","TestUser1","test1","testuser1@gmail.com","87654321",new Date(),false);
        when(userRepository.findByUserName("TestUser1")).thenReturn(user);
        TweetRequest tweetRequest=new TweetRequest("TestTweet","Testing tweet App");
        List<String> comments = new ArrayList<>();
        Tweet tweet= new Tweet("1","TestTweet","Testing tweet App",0,user,new Date(),comments);
        when(tweetRepository.save(tweetMapper.map(tweetRequest, user))).thenReturn(tweet);
        assertTrue(tweetService.updateTweet("1","TestUser1",tweetRequest));
    }

    @Test
    void getAllTweets() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        User user2 = new User("2","TestUser2","test2","testuser2@gmail.com","12345678",new Date(),true);
        List<String> comments = new ArrayList<>();
        comments.add("Good");
        Tweet tweet1 = new Tweet("1","Tweet1","Test Tweet1",3,user1, new Date(),comments);
        Tweet tweet2 = new Tweet("2","Tweet2","Test Tweet2",0,user2,new Date(),comments);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        when(tweetRepository.findAll()).thenReturn(tweets);
        assertEquals(tweetService.getAllTweets(),tweets);
    }

    @Test
    void getTweetsByUserName() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        when(userRepository.findByUserName("TestUser1")).thenReturn(user1);
        List<String> comments = new ArrayList<>();
        comments.add("Good");
        Tweet tweet1 = new Tweet("1","Tweet1","Test Tweet1",3,user1, new Date(),comments);
        Tweet tweet2 = new Tweet("2","Tweet2","Test Tweet2",0,user1,new Date(),comments);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        when(tweetRepository.findByUser(user1)).thenReturn(tweets);
        assertEquals(tweetService.getTweetsByUserName("TestUser1"),tweets);
    }

    @Test
    void deleteTweetById() {
        tweetRepository.deleteById("1");
        assertTrue(tweetService.deleteTweetById("1"));
    }

    @Test
    void replyTweetById() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        List<String> comments = new ArrayList<>();
        comments.add("Good");
        Tweet tweet1 = new Tweet("1","Tweet1","Test Tweet1",3,user1, new Date(),comments);
        when(tweetRepository.findById("1")).thenReturn(Optional.of(tweet1));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet1);
        CommentsDto commentsDto=new CommentsDto("1",Instant.now(),"Good","TestUser1");
        assertTrue(tweetService.replyTweetById("1","Nice"));
    }

    @Test
    void voteTweetById() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        List<String> comments = new ArrayList<>();
        comments.add("Good");
        Tweet tweet1 = new Tweet("1","Tweet1","Test Tweet1",3,user1, new Date(),comments);
        when(tweetRepository.findById("1")).thenReturn(Optional.of(tweet1));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet1);
        assertTrue(tweetService.voteTweetById("1"));
    }
}