package com.tweetapp.tweetappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class NotificationEmail {
    private String subject;
    private String receipient;
    private String body;
}
