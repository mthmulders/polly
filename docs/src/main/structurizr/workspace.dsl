workspace polly {
    model {
        speaker = person "Speaker" "Someone presenting at a conference or meetup"
        attendee = person "Attendee" "Someone attending a conference or meetup"
        polly = softwareSystem "Polly" "A web application for running a live poll during an event"

        speaker -> polly "Define poll"
        speaker -> polly "Conduct poll"
        attendee -> polly "Participate in poll"
    }
    views {
        theme default
    }
}