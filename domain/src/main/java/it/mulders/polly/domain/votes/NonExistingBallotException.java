package it.mulders.polly.domain.votes;

public class NonExistingBallotException extends IllegalArgumentException {
    public NonExistingBallotException(String pollSlug, String ticketId) {
        super(String.format("Unknown ballot %s for poll %s", ticketId, pollSlug));
    }
}
