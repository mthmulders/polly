package it.mulders.polly.domain.polls;

import java.util.Optional;

public interface PollInstanceRepository {
    Optional<PollInstance> findByPollSlugAndInstanceSlug(String pollSlug, String pollInstanceSlug);
}
