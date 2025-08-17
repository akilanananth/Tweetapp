package com.tweetapp.tweetappbackend.service;

import com.tweetapp.tweetappbackend.dto.CommentsDto;
import com.tweetapp.tweetappbackend.dto.TweetRequest;
import com.tweetapp.tweetappbackend.exception.TweetAppException;
import com.tweetapp.tweetappbackend.exception.TweetNotFoundException;
import com.tweetapp.tweetappbackend.exception.UserNotFoundException;
import com.tweetapp.tweetappbackend.mapper.TweetMapper;
import com.tweetapp.tweetappbackend.model.*;
import com.tweetapp.tweetappbackend.repository.TweetRepository;
import com.tweetapp.tweetappbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TweetService {
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private TweetMapper tweetMapper;
    @Autowired
    private UserRepository userRepository;

    private final Logger logger= LoggerFactory.getLogger(TweetService.class);

    @Transactional(readOnly = true)
    public boolean saveTweet(String userName, TweetRequest tweetRequest) {
        try {

            User user = userRepository.findByUserName(userName);
            tweetRepository.save(tweetMapper.map(tweetRequest, user));
            logger.info("Tweet saved successfully");
        }catch(Exception exception){
            throw new UserNotFoundException("User Not Found");
        }
        return true;
    }

    public boolean updateTweet(String id,String userName, TweetRequest tweetRequest) {
        try {
            User user = userRepository.findByUserName(userName);
            Tweet tweet = tweetRepository.findByTweetId(id);
            tweet.setTweetName(tweetRequest.getTweetName());
            tweet.setDescription(tweetRequest.getDescription());
            tweetRepository.save(tweet);
            logger.info("Tweet updated successfully");
        }catch(Exception exception){
            throw new UserNotFoundException("User Not Found");
        }
        return true;
    }

    public List<Tweet> getAllTweets() {
        try {
            logger.info("Getting tweets");
            return tweetRepository.findAll();
        }catch(Exception exception){
            throw new TweetNotFoundException("Tweet Not Found");
        }

    }

    public Tweet getTweetById(String id) {
            logger.info("getting tweet by id");
            return tweetRepository.findByTweetId(id);
    }

    public List<Tweet> getTweetsByUserName(String userName) {
        try {
            User user = userRepository.findByUserName(userName);
            logger.info("getting tweet by username");
            return tweetRepository.findByUser(user);
        }catch(Exception exception){
            throw new TweetNotFoundException("Tweet Not Found");
        }

    }

    @Transactional
    public boolean deleteTweetById(String tweetId) {
        try {
            logger.info("Tweet deleted successfully");
            tweetRepository.deleteById(tweetId);
        }catch(Exception e){
            throw new TweetAppException("Deletion not complete");
        }
        return true;
    }

    @Transactional
    public boolean replyTweetById(String id,String comment) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new TweetAppException("Post Not Found with ID - " + id));
        List<String> comments;
        if(tweet.getComments()==null) {
            comments = new ArrayList<>();
        }else {
            comments = tweet.getComments();
        }
        comments.add(comment);
        tweet.setComments(comments);
        tweetRepository.save(tweet);
        logger.info("Tweet commented successfully");
        return true;
    }

    @Transactional
    public boolean voteTweetById(String id) {
        Tweet tweet = tweetRepository.findById(id)
                .orElseThrow(() -> new TweetAppException("Post Not Found with ID - " + id));
            tweet.setVoteCount(tweet.getVoteCount() + 1);
        tweetRepository.save(tweet);
        return true;
    }

//    public List<TweetRequest> getCommentsById(String id) {
//        logger.info("getting tweet by id");
//        tweetRepository.findById(id)
//                .orElseThrow(() -> new TweetAppException("Post Not Found with ID - " + id));
//    }
//
//    public List<TweetRequest> getCommentsByUserName(String username) {
//        try {
//            User user = userRepository.findByUserName(userName);
//            logger.info("getting tweet by username");
//            return tweetRepository.findByUser(user);
//        }catch(Exception exception){
//            throw new TweetNotFoundException("Tweet Not Found");
//        }
}