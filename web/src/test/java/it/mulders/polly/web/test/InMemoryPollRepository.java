package it.mulders.polly.web.test;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import java.util.Optional;
import java.util.Set;

public class InMemoryPollRepository implements PollRepository {
    private Set<Poll> entities;

    public InMemoryPollRepository(Set<Poll> entities) {
        this.entities = entities;
    }

    @Override
    public Optional<Poll> findBySlug(String slug) {
        return entities.stream().filter(poll -> slug.equals(poll.getSlug())).findAny();
    }
}
