package it.mulders.polly.domain.polls;

import it.mulders.polly.domain.impl.RandomStringUtils;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.Vote;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Aggregate root for the <strong>Polls</strong> domain. A <strong>Poll</strong> is a question where participants can
 * cast their votes from a predefined set of possible answers.
 */
public class Poll {
    private final String question;
    private final String slug;
    private final Set<Option> options;
    private final Set<Ballot> ballots;
    private final Set<Vote> votes;

    public Poll(String question, String slug, Set<Option> options) {
        this(question, slug, options, new HashSet<>(), new HashSet<>());
    }

    protected Poll(String question, String slug, Set<Option> options, Set<Ballot> ballots, Set<Vote> votes) {
        this.slug = Objects.requireNonNull(slug, "Slug must not be null");
        this.question = Objects.requireNonNull(question, "Question must not be null");
        this.options = Objects.requireNonNull(options, "Options must not be null");
        this.ballots = Objects.requireNonNull(ballots, "Ballots must not be null");
        this.votes = Objects.requireNonNull(votes, "Votes must not be null");
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

    public Optional<Option> selectOption(Integer optionValue) {
        return options.stream()
                .filter(option -> option.getOptionValue().equals(optionValue))
                .findAny();
    }

    public Set<Vote> getVotes() {
        return Collections.unmodifiableSet(votes);
    }

    public Set<Ballot> getBallots() {
        return Collections.unmodifiableSet(ballots);
    }

    public Optional<Ballot> findBallotByTicketId(String ticketId) {
        return ballots.stream()
                .filter(ballot -> ballot.getTicketId().equals(ticketId))
                .findAny();
    }

    public Ballot requestBallot(String clientIdentifier) {
        var ballot = new Ballot(clientIdentifier, RandomStringUtils.generateRandomIdentifier(8));
        ballots.add(ballot);
        return ballot;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Poll other)) return false;

        return Objects.equals(this.question, other.question)
                && Objects.equals(this.slug, other.slug)
                && Objects.equals(this.options, other.options)
                && Objects.equals(this.votes, other.votes)
                && Objects.equals(this.ballots, other.ballots);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(question, slug, options, ballots, votes);
    }

    public void registerVote(Vote vote) {
        if (!ballots.contains(vote.getBallot())) {
            throw new IllegalArgumentException("Ballot belongs to a different poll");
        }
        if (!options.contains(vote.getOption())) {
            throw new IllegalArgumentException("Option belongs to a different poll");
        }
        vote.getBallot().markAsUsed();
        this.votes.add(vote);
    }

    public boolean isBallotUsed(Ballot ballot) {
        var ticketId = ballot.getTicketId();
        return this.votes.stream()
                .map(Vote::getBallot)
                .map(Ballot::getTicketId)
                .anyMatch(ticketId::equals);
    }
}
