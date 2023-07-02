package it.mulders.polly.domain.polls;

import it.mulders.polly.domain.Sluggable;

public record Poll(String question, String slug) implements Sluggable {
}
