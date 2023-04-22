package it.mulders.polly.domain;

public record Poll(String question, String slug) implements Sluggable {
}
