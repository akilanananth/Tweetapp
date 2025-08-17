package com.tweetapp.tweetappbackend.controller;

import com.tweetapp.tweetappbackend.dto.CommentsDto;
import com.tweetapp.tweetappbackend.dto.TweetRequest;
import com.tweetapp.tweetappbackend.model.Tweet;
import com.tweetapp.tweetappbackend.model.User;
//import com.tweetapp.tweetappbackend.service.ProducerService;
import com.tweetapp.tweetappbackend.service.TweetService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class TweetControllerTest {

    @Mock
    TweetService tweetService;

    //@Mock
    //ProducerService producerService;

    @InjectMocks
    TweetController tweetController;

    @Test
    void testPostTweet(){
        tweetService.saveTweet(eq("TestUser"),any(TweetRequest.class));
        TweetRequest tweetRequest = new TweetRequest("SampleTweet","Testing Tweet app");
        //producerService.sendMessage("Tweet Test sent");
        ResponseEntity<String> responseEntity = tweetController.postTweet("TestUser",tweetRequest);
        assertEquals(responseEntity.getStatusCodeValue(),200);
    }

    @Test
    void getTweets() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        User user2 = new User("2","TestUser2","test2","testuser2@gmail.com","12345678",new Date(),true);
        List<String> comments = new ArrayList<>();
        comments.add("Good");
        Tweet tweet1 = new Tweet("1","Tweet1","Test Tweet1",3,user1, new Date(),comments);
        Tweet tweet2 = new Tweet("2","Tweet2","Test Tweet2",0,user2,new Date(),comments);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        when(tweetService.getAllTweets()).thenReturn(tweets);
        assertEquals(tweetController.getTweets().getBody(),tweets);
        assertEquals(tweetController.getTweets().getStatusCodeValue(),200);
    }

    @Test
    void tweetsByUserName() {
        User user1 = new User("1","TestUser1","test1","testuser1@gmail.com","87654321", new Date(),true);
        List<String> comments = new ArrayList<>();
        comments.add("Good");
        Tweet tweet1 = new Tweet("1","Tweet1","Test Tweet1",3,user1, new Date(),comments);
        Tweet tweet2 = new Tweet("2","Tweet2","Test Tweet2",0,user1,new Date(),comments);
        List<Tweet> tweets = new ArrayList<>();
        tweets.add(tweet1);
        tweets.add(tweet2);
        when(tweetService.getTweetsByUserName("TestUser1")).thenReturn(tweets);
        assertEquals(tweetController.tweetsByUserName("TestUser1").getBody(),tweets);
        assertEquals(tweetController.tweetsByUserName("TestUser1").getStatusCodeValue(),200);
    }

    @Test
    void updateTweetByUser() {
        tweetService.updateTweet(eq("1"),eq("TestUser"),any(TweetRequest.class));
        TweetRequest tweetRequest = new TweetRequest("SampleTweet","Testing Tweet app");
        ResponseEntity<String> responseEntity = tweetController.updateTweetByUser("TestUser","1",tweetRequest);
        assertEquals(responseEntity.getStatusCodeValue(),200);
    }

    @Test
    void deleteTweet() {
        tweetService.deleteTweetById(eq("1"));
        ResponseEntity<String> responseEntity = tweetController.deleteTweet("TestUser","1");
        assertEquals(responseEntity.getStatusCodeValue(),200);
    }

    @Test
    void voteTweetByUser() {
        tweetService.voteTweetById(eq("1"));
        ResponseEntity<String> responseEntity = tweetController.voteTweetByUser("TestUser","1");
        assertEquals(responseEntity.getStatusCodeValue(),200);
    }

    @Test
    void replyTweetByUser() {
        tweetService.replyTweetById(eq("1"),"Nice");
        CommentsDto commentsDto = new CommentsDto("1",Instant.now(),"Congratulations!","TestUser");
        ResponseEntity<String> responseEntity = tweetController.replyTweetByUser("TestUser","1","Nice");
        assertEquals(responseEntity.getStatusCodeValue(),200);
    }
}