package it.mulders.polly.web.test;

import it.mulders.polly.domain.polls.PollInstance;
import it.mulders.polly.domain.polls.PollInstanceRepository;

import java.util.Optional;
import java.util.Set;

public class InMemoryPollInstanceRepository implements PollInstanceRepository {
    private Set<PollInstance> entities;

    public InMemoryPollInstanceRepository(Set<PollInstance> entities) {
        this.entities = entities;
    }
    @Override
    public Optional<PollInstance> findByPollSlugAndInstanceSlug(String pollSlug, String pollInstanceSlug) {
        return entities.stream()
                .filter(pollInstance -> pollSlug.equals(pollInstance.poll().slug()))
                .filter(pollInstance -> pollInstanceSlug.equals(pollInstance.slug()))
                .findAny();
    }
}
