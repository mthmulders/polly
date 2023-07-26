package it.mulders.polly.domain.polls;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate root for the <strong>Polls</strong> domain. A <strong>Poll</strong> is a question where participants can
 * cast their votes from a predefined set of possible answers.
 */
public class Poll {
    private final String question;
    private final String slug;
    private final Set<Option> options;

    public Poll(String question, String slug, Set<Option> options) {
        this.slug = Objects.requireNonNull(slug, "Slug must not be null");
        this.question = Objects.requireNonNull(question, "Question must not be null");
        this.options = Objects.requireNonNull(options, "Options must not be null");
    }

    public String getQuestion() {
        return question;
    }

    public String getSlug() {
        return slug;
    }

    public Set<Option> getOptions() {
        return Collections.unmodifiableSet(options);
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Poll other)) return false;

        return this.question.equals(other.question) && this.slug.equals(other.slug) && this.options.equals(other.options);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(question, slug, options);
    }
}
