workspace polly {
    model {
        speaker = person "Speaker" "Someone presenting at a conference or meetup"
        attendee = person "Attendee" "Someone attending a conference or meetup"
        polly = softwareSystem "Polly" "A web application for running a live poll during an event"

        speaker -> polly "Define, run and view poll"
        attendee -> polly "Vote and view poll"
    }
    views {
        systemContext "Polly" "Context" {
            include speaker attendee polly
            autoLayout
        }
        theme default
    }
}