package it.mulders.polly.domain;

public record PollInstance(Poll poll, String slug) implements Sluggable {
}
