package it.mulders.polly.domain.impl;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class RandomStringUtilsTest implements WithAssertions {
    @Nested
    class GenerateRandomIdentifier {
        @Test
        void should_generate_string_of_specified_length() {
            var result = RandomStringUtils.generateRandomIdentifier(3);

            assertThat(result.length()).isEqualTo(3);
        }

        @Test
        void contains_no_characters_that_are_forbidden() {
            // copied from implementation
            var allowedChars = List.of(new Character[] {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                    'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            });

            var result = RandomStringUtils.generateRandomIdentifier(1024).toCharArray();

            for (char c : result) {
                assertThat(allowedChars.contains(c)).isTrue();
            }
        }
    }
}