package it.mulders.polly.infra;

import it.mulders.polly.infra.polls.PollEntity;
import it.mulders.polly.infra.polls.PollOptionEntity;
import java.util.Set;
import java.util.UUID;
import nl.jqno.equalsverifier.ConfiguredEqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public abstract class AbstractEntityTest {
    private final PollEntity poll1 = preparePollEntity(1);
    private final PollEntity poll2 = preparePollEntity(2);

    protected ConfiguredEqualsVerifier equalsVerifier = EqualsVerifier.configure()
            .withPrefabValues(PollEntity.class, poll1, poll2)
            .suppress(Warning.IDENTICAL_COPY_FOR_VERSIONED_ENTITY, Warning.SURROGATE_KEY, Warning.STRICT_HASHCODE);

    private PollEntity preparePollEntity(int id) {
        var entity = new PollEntity();
        entity.setId(UUID.randomUUID());
        entity.setSlug("slug-" + id);
        entity.setQuestion("Poll " + id);
        entity.setOptions(Set.of(preparePollOptionEntity(entity, 1), preparePollOptionEntity(entity, 2)));
        return entity;
    }

    private PollOptionEntity preparePollOptionEntity(PollEntity poll, int id) {
        var entity = new PollOptionEntity();
        entity.setId(UUID.randomUUID());
        entity.setPoll(poll);
        entity.setDisplayValue("Option " + id);
        return entity;
    }
}
