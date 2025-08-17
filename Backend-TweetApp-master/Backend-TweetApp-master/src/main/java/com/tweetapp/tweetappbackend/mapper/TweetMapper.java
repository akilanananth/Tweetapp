package com.tweetapp.tweetappbackend.mapper;

import com.tweetapp.tweetappbackend.dto.TweetRequest;
import com.tweetapp.tweetappbackend.model.Tweet;
import com.tweetapp.tweetappbackend.model.User;

import com.tweetapp.tweetappbackend.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TweetMapper {

    @Autowired
    private UserService userService;

    //Mapping(target = "tweetId", source = "tweetRequest.tweetId")
    @Mapping(target = "createdDate", expression = "java(new java.util.Date())")
    @Mapping(target = "description", source = "tweetRequest.description")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Tweet map(TweetRequest tweetRequest, User user);

}