package com.tweetapp.tweetappbackend.exception;

public class TweetNotFoundException extends RuntimeException{
    public TweetNotFoundException(String message){
        super(message);
    }
}
