package it.mulders.polly.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HomepageIT extends AbstractPlaywrightTest {
    @Test
    void should_display_homepage() {
        page.navigate("http://localhost:9080/");

        assertThat(page.textContent("//main")).contains("Polly");
    }
}
