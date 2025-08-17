package com.tweetapp.tweetappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
//    private String id;
    private String tweetId;
    private Instant createdDate;
    private String text;
    private String userName;
}