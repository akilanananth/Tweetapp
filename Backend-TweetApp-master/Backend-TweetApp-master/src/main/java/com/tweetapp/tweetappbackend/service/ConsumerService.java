package com.tweetapp.tweetappbackend.service;

import com.tweetapp.tweetappbackend.model.Tweet;
import com.tweetapp.tweetappbackend.repository.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ConsumerService
{
    @Autowired
    TweetRepository tweetRepository;
    @KafkaListener(topics = "TweetLogs",
            groupId = "group_id1")
    public void consume(String message) {
        log.info(message);

    }

    @KafkaListener(topics = "TweetMessage",
            groupId = "group_id2",
            containerFactory = "userKafkaListenerContainerFactory")
    public Tweet consumeTweet(Tweet tweet) {
        tweetRepository.save(tweet);
        log.info("Consumed message "+tweet.toString());
        return tweet;

    }
}