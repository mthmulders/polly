package it.mulders.polly.domain.votes;

public class NonExistingOptionException extends IllegalArgumentException {
    public NonExistingOptionException(String pollSlug, Integer option) {
        super(String.format("Unknown option %s for poll %s", option, pollSlug));
    }
}
