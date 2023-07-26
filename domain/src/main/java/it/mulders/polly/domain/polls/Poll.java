package it.mulders.polly.domain.polls;

/**
 * Aggregate root for the <strong>Polls</strong> domain. A <strong>Poll</strong> is a question where participants can
 * cast their votes from a predefined set of possible answers.
 */
public class Poll {
    private String question;
    private String slug;

    public Poll(String question, String slug) {
        this.slug = slug;
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public String getSlug() {
        return slug;
    }
}
