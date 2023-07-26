package it.mulders.polly.domain.polls;

import java.util.Objects;

/**
 * Aggregate root for the <strong>Polls</strong> domain. A <strong>Poll</strong> is a question where participants can
 * cast their votes from a predefined set of possible answers.
 */
public class Poll {
    private String question;
    private String slug;

    public Poll(String question, String slug) {
        this.slug = Objects.requireNonNull(slug, "Slug must not be null");
        this.question = Objects.requireNonNull(question, "Question must not be null");
    }

    public String getQuestion() {
        return question;
    }

    public String getSlug() {
        return slug;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Poll other)) return false;

        return this.question.equals(other.question) && this.slug.equals(other.slug);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(question, slug);
    }
}
