package it.mulders.polly.domain.polls;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import it.mulders.polly.domain.impl.RandomStringUtils;
import it.mulders.polly.domain.votes.Ballot;
import it.mulders.polly.domain.votes.Vote;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    public Map<Option, BigDecimal> calculateVotePercentages() {
        var voteCount = BigDecimal.valueOf(votes.size());
        var voteCountByOption = votes.stream().collect(groupingBy(Vote::getOption, counting()));
        var percentageOfVotes = (Function<Option, BigDecimal>) (Option option) -> {
            var count = BigDecimal.valueOf(voteCountByOption.getOrDefault(option, 0L));
            var fraction = count.divide(voteCount, 3, RoundingMode.HALF_EVEN);
            var percentage = fraction.multiply(ONE_HUNDRED);
            // Formatting: at most 1 decimal place, but none if it's zero. See unit test for details.
            // Inspired by https://stackoverflow.com/a/64597273/1523342.
            percentage = percentage.stripTrailingZeros();
            if (percentage.scale() < 0)
                percentage = percentage.setScale(0, RoundingMode.UNNECESSARY);
            return percentage;
        };
        var percentageByOption = voteCountByOption.keySet().stream().collect(toMap(identity(), percentageOfVotes));

        // If an option didn't get any votes, it will be missing in the map by now.
        options.forEach(option -> percentageByOption.computeIfAbsent(option, ignored -> BigDecimal.ZERO));

        return percentageByOption;
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
        return this.votes.stream().map(Vote::getBallot).map(Ballot::getTicketId).anyMatch(ticketId::equals);
    }
}
