package it.mulders.polly.infra.polls;

import it.mulders.polly.infra.AbstractEntityTest;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PollOptionEntityTest extends AbstractEntityTest implements WithAssertions {
    @Test
    void honours_equals_contract() {
        equalsVerifier.forClass(PollOptionEntity.class).verify();
    }
}
