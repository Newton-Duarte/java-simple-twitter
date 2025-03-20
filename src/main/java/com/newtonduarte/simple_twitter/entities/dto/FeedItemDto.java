package com.newtonduarte.simple_twitter.entities.dto;

public record FeedItemDto(
        Long tweetId,
        String content,
        String author
) {
}
