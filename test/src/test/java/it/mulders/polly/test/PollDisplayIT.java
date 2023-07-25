package it.mulders.polly.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PollDisplayIT extends AbstractPlaywrightTest {
    @Test
    void should_display_question() {
        page.navigate("http://localhost:9080/show/jakarta-mvc");

        assertThat(page.textContent("//main")).contains("Do you like Jakarta MVC?");
    }
}
