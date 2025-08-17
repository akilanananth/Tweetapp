package com.tweetapp.tweetappbackend.service;

import com.tweetapp.tweetappbackend.model.Tweet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service

public class ProducerService {
    private static final Logger log = LogManager.getLogger(ProducerService.class);
    private String topicName = "TweetLogs";
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private String userTopicName="TweetMessage";

    @Autowired
    private KafkaTemplate<String, Tweet> userKafkaTemplate;

    public void sendMessage(String message)
    {
        log.info("Sending Message to the consumer.....");
        kafkaTemplate.send(topicName, message);
    }

    public void postTweet(Tweet tweet)
    {
        log.info("Sending Tweet to the consumer.....");
        userKafkaTemplate.send(userTopicName, tweet);
    }
}
