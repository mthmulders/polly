package it.mulders.polly.domain;

import java.util.Optional;

public interface PollInstanceRepository {
    Optional<PollInstance> findBySlugs(String pollSlug, String pollInstanceSlug);
}
