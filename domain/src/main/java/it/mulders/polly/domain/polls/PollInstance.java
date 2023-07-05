package it.mulders.polly.domain.polls;

import it.mulders.polly.domain.Sluggable;

public record PollInstance(Poll poll, String slug) implements Sluggable {
}
