package it.mulders.polly.infra.votes;

import it.mulders.polly.infra.AbstractEntityTest;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BallotEntityTest extends AbstractEntityTest implements WithAssertions {
    @Test
    void honours_equals_contract() {
        equalsVerifier.forClass(BallotEntity.class).verify();
    }
}
