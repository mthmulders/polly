package it.mulders.polly.web.display;

import static java.util.Comparator.comparing;

import it.mulders.polly.domain.polls.Poll;
import it.mulders.polly.web.display.dto.OptionWithPercentage;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class VoteSummaryService {
    private static final Logger log = Logger.getLogger(VoteSummaryService.class.getName());

    public List<OptionWithPercentage> calculateVotePercentages(final Poll poll) {
        var votePercentages = poll.calculateVotePercentages();
        return votePercentages.entrySet().stream()
                .map(e -> new OptionWithPercentage(e.getKey().getDisplayValue(), e.getValue()))
                .sorted(comparing(OptionWithPercentage::percentage).reversed())
                .toList();
    }

    public int calculateVoteCount(final Poll poll) {
        return poll.getVotes().size();
    }
}
