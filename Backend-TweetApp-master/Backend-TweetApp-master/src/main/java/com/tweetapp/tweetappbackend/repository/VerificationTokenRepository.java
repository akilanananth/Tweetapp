package com.tweetapp.tweetappbackend.repository;

import com.tweetapp.tweetappbackend.model.User;
import com.tweetapp.tweetappbackend.model.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends MongoRepository<VerificationToken,String> {
    Optional<VerificationToken> findByToken(String token);
}
