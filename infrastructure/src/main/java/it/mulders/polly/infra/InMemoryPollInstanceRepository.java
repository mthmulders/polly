package it.mulders.polly.infra;

import it.mulders.polly.domain.Poll;
import it.mulders.polly.domain.PollInstance;
import it.mulders.polly.domain.PollInstanceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class InMemoryPollInstanceRepository implements PollInstanceRepository {
    // poll-slug -> instance-slug -> domain object
    private final Map<String, Map<String, PollInstance>> data = new HashMap<>();

    public InMemoryPollInstanceRepository() {
        data.put("demo", Map.of("demo", new PollInstance(new Poll("Do you like me?", "demo"), "demo")));
    }

    @Override
    public Optional<PollInstance> findBySlugs(String pollSlug, String pollInstanceSlug) {
        if (data.containsKey(pollSlug)) {
            var poll = data.get(pollSlug);
            if (poll.containsKey(pollInstanceSlug)) {
                return Optional.of(poll.get(pollInstanceSlug));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }
}
