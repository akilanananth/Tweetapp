package com.tweetapp.tweetappbackend.controller;

import com.tweetapp.tweetappbackend.dto.TweetRequest;
import com.tweetapp.tweetappbackend.model.Tweet;
import com.tweetapp.tweetappbackend.service.ProducerService;
import com.tweetapp.tweetappbackend.service.TweetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin
public class TweetController {

    @Autowired
    private TweetService tweetService;
    @Autowired
    private ProducerService producerService;

    private final Logger logger= LoggerFactory.getLogger(TweetController.class);

    @PostMapping("/{username}/add")
    public  ResponseEntity<String> postTweet(@PathVariable String username, @RequestBody TweetRequest tweetRequest){
        tweetService.saveTweet(username,tweetRequest);
        producerService.sendMessage("user added");
        return new ResponseEntity<>("Tweet created successfully!!!",HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tweet>> getTweets(){
        logger.info("Displayed Tweets");
        return new ResponseEntity<>(tweetService.getAllTweets(), HttpStatus.OK);
    }

    @GetMapping("/tweet/{id}")
    public ResponseEntity<Tweet> tweetById(@PathVariable String id){
        logger.info("Displayed Tweets by id");
        return new ResponseEntity<>(tweetService.getTweetById(id), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Tweet>> tweetsByUserName(@PathVariable String username){
        logger.info("Displayed Tweets by username");
        return new ResponseEntity<>(tweetService.getTweetsByUserName(username), HttpStatus.OK);
    }

    @PutMapping("/{username}/update/{id}")
    public ResponseEntity<String> updateTweetByUser(@PathVariable String username,@PathVariable String id,@RequestBody TweetRequest tweetRequest){
        tweetService.updateTweet(id,username,tweetRequest);
        logger.info("Updated Tweet");
        return new ResponseEntity<>("Updated Tweet successfully!!!",HttpStatus.OK);
    }

    @DeleteMapping("/{username}/delete/{id}")
    public ResponseEntity<String> deleteTweet(@PathVariable String username,@PathVariable String id){
        tweetService.deleteTweetById(id);
        logger.info("Deleted Tweet");
        return new ResponseEntity<>("Deleted Tweet successfully!!!",HttpStatus.OK);
    }

    @PutMapping("/{username}/like/{id}")
    public ResponseEntity<String> voteTweetByUser(@PathVariable String username, @PathVariable String id){
        tweetService.voteTweetById(id);
        logger.info("Liked Tweet");
        return new ResponseEntity<>("Voted Tweet successfully!!!",HttpStatus.OK);
    }

    @PostMapping("/{username}/reply/{id}")
    public ResponseEntity<String> replyTweetByUser(@PathVariable String username, @PathVariable String id, @RequestBody String comment){
        tweetService.replyTweetById(id,comment);
        logger.info("Commented Tweet");
        return new ResponseEntity<>("Voted Tweet successfully!!!",HttpStatus.OK);
    }

}
