package com.newtonduarte.simple_twitter.controllers;

import com.newtonduarte.simple_twitter.entities.Role;
import com.newtonduarte.simple_twitter.entities.Tweet;
import com.newtonduarte.simple_twitter.entities.dto.CreateTweetDto;
import com.newtonduarte.simple_twitter.entities.dto.FeedDto;
import com.newtonduarte.simple_twitter.entities.dto.FeedItemDto;
import com.newtonduarte.simple_twitter.repositories.TweetRepository;
import com.newtonduarte.simple_twitter.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping(path = "/tweets")
public class TweetController {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/feed")
    public ResponseEntity<FeedDto> getTweetFeed(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        var tweets = tweetRepository.findAll(
                PageRequest.of(page, pageSize, Sort.Direction.DESC, "created_at")
        ).map(tweet -> new FeedItemDto(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getName()
        ));

        return ResponseEntity.ok(new FeedDto(
                tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements()
        ));
    }

    @PostMapping
    public ResponseEntity<Void> createTweet(
            @RequestBody CreateTweetDto createTweetDto,
            JwtAuthenticationToken token
    ) {
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(createTweetDto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTweet(
            @PathVariable Long id,
            JwtAuthenticationToken token
    ) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var isAdmin = user.get().getRoles()
                .stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
        var tweet = tweetRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (isAdmin || tweet.getUser().getId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.deleteById(id);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }
}
