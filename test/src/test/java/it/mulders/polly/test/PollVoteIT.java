package it.mulders.polly.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PollVoteIT extends AbstractPlaywrightTest {
    @Test
    void should_display_question() {
        page.navigate("http://localhost:9080/vote/jakarta-mvc");

        assertThat(page.textContent("//main/p")).contains("Do you like Jakarta MVC?");
    }

    @Test
    void should_display_options() {
        page.navigate("http://localhost:9080/vote/jakarta-mvc");

        var options = page.textContent("//main/form");

        assertThat(options).contains("Yes!").contains("Of course!");
    }

    @Test
    void should_accept_vote() {
        page.navigate("http://localhost:9080/vote/jakarta-mvc");

        page.click("//label[@for='option-1']");
        page.click("//input[@type='submit']");

        var text = page.textContent("//main");

        assertThat(text).contains("Thanks for your vote").contains("You voted Yes!");
    }

    @Test
    void should_not_accept_double_vote() {
        page.navigate("http://localhost:9080/vote/jakarta-mvc");

        page.click("//label[@for='option-1']");
        page.click("//input[@type='submit']");

        page.navigate("http://localhost:9080/vote/jakarta-mvc");

        var text = page.textContent("//main");

        assertThat(text).contains("You have already voted, thanks for your participation!");
    }
}
