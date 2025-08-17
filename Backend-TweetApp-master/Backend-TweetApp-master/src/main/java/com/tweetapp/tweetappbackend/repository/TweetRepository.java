package com.tweetapp.tweetappbackend.repository;

import com.tweetapp.tweetappbackend.model.Tweet;
import com.tweetapp.tweetappbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<Tweet,String> {
    List<Tweet> findByUser(User user);
    Tweet findByTweetId(String id);
    void deleteByTweetId(String id);
    List<Tweet> findByUserUserName(String username);
}
