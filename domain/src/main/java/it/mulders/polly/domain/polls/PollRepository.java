package it.mulders.polly.domain.polls;

import java.util.Optional;

public interface PollRepository {
    Optional<Poll> findBySlug(String slug);
}
