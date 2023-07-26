package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.PollRepository;
import it.mulders.polly.infra.MapStructHelper;
import it.mulders.polly.infra.database.AbstractJpaRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JpaPollRepositoryIT extends AbstractJpaRepositoryTest<PollRepository, JpaPollRepository> {
    @BeforeEach
    void prepare() {
        prepare(em -> new JpaPollRepository(em, MapStructHelper.getMapper(PollMapper.class)));
    }

    @Test
    void lookup_using_slugs_should_succeed() {
        var original = preparePoll("What's up?", "test-poll");

        var result = repository.findBySlug("test-poll");

        assertThat(result).isPresent().hasValueSatisfying(poll -> {
            assertThat(poll.getSlug()).isEqualTo(original.getSlug());
            assertThat(poll.getQuestion()).isEqualTo(original.getQuestion());
        });
    }

    private PollEntity preparePoll(String question, String slug) {
        var poll = new PollEntity();
        poll.setSlug(slug);
        poll.setQuestion(question);

        var transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.persist(poll);
            transaction.commit();
        } catch (Throwable t) {
            transaction.rollback();
            fail("Could not prepare database entities", t);
        }

        return poll;
    }
}
