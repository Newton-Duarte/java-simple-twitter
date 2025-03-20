package com.newtonduarte.simple_twitter.repositories;

import com.newtonduarte.simple_twitter.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
