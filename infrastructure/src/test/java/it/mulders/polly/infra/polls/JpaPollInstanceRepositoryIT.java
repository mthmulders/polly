package it.mulders.polly.infra.polls;

import it.mulders.polly.domain.polls.PollInstanceRepository;
import it.mulders.polly.infra.MapStructHelper;
import it.mulders.polly.infra.database.AbstractJpaRepositoryTest;
import org.junit.jupiter.api.BeforeEach;

class JpaPollInstanceRepositoryIT extends AbstractJpaRepositoryTest<PollInstanceRepository, JpaPollInstanceRepository> {
    @BeforeEach
    void prepare() {
        prepare(em -> new JpaPollInstanceRepository(em, MapStructHelper.getMapper(PollMapper.class)));
    }
}
