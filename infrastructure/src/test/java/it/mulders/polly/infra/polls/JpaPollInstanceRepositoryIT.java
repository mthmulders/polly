package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.PollInstanceRepository;
import it.mulders.polly.infra.MapStructHelper;
import it.mulders.polly.infra.database.AbstractJpaRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JpaPollInstanceRepositoryIT extends AbstractJpaRepositoryTest<PollInstanceRepository, JpaPollInstanceRepository> {
    @BeforeEach
    void prepare() {
        prepare(em -> new JpaPollInstanceRepository(em, MapStructHelper.getMapper(PollMapper.class)));
    }

    @Test
    void lookup_using_slugs_should_succeed() {
        preparePollWithInstance("test-poll", "test-instance");

        var result = repository.findByPollSlugAndInstanceSlug("test-poll", "test-instance");

        assertThat(result).isPresent().hasValueSatisfying(pollInstance -> {
            assertThat(pollInstance.slug()).isEqualTo("test-instance");
            assertThat(pollInstance.poll().slug()).isEqualTo("test-poll");
        });
    }

    private PollEntity preparePollWithInstance(String pollSlug, String instanceSlug) {
        var poll = new PollEntity();
        poll.setSlug(pollSlug);
        poll.setQuestion("What's up?");

        var pollInstance = new PollInstanceEntity();
        pollInstance.setPoll(poll);
        pollInstance.setSlug(instanceSlug);

        var transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.persist(poll);
            entityManager.persist(pollInstance);
            transaction.commit();
        } catch (Throwable t) {
            transaction.rollback();
            fail("Could not prepare database entities", t);
        }

        return poll;
    }
}
