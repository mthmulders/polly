package it.mulders.polly.infra;

import it.mulders.polly.infra.polls.PollEntity;

import java.util.UUID;

public interface PollRelatableEntity extends IdentifiableEntity<UUID> {
    void setPoll(PollEntity poll);
}
