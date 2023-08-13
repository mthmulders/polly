package it.mulders.polly.web.test;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.domain.polls.PollRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class InMemoryPollRepository extends HashSet<Poll> implements PollRepository {

    public InMemoryPollRepository(Collection<Poll> c) {
        super(c);
    }

    @Override
    public Optional<Poll> findBySlug(String slug) {
        return stream().filter(poll -> slug.equals(poll.getSlug())).findAny();
    }
}
