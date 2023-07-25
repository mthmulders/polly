package it.mulders.polly.web.home;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HomeControllerTest implements WithAssertions {
    private final HomeController controller = new HomeController();

    @Test
    void should_render_view() {
        assertThat(controller.show()).isEqualTo(HomeController.Views.HOME);
    }
}
