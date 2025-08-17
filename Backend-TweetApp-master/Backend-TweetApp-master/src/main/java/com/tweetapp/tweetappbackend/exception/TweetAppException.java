package com.tweetapp.tweetappbackend.exception;

public class TweetAppException extends RuntimeException{
    public TweetAppException(String message){
        super(message);
    }
    public TweetAppException(String message,Exception exception){
        super(message,exception);
    }
}
