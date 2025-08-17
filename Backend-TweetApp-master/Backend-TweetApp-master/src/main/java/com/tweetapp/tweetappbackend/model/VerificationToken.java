package com.tweetapp.tweetappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("tokens")
public class VerificationToken {
    @Id
    private String id;
    private  String token;
    private User user;
    private Instant expiryDate;
}
