workspace polly {
    !identifiers hierarchical

    model {
        speaker = person "Speaker" "Someone presenting at a conference or meetup"
        attendee = person "Attendee" "Someone attending a conference or meetup"
        polly = softwareSystem "Polly" "A web application for running a live poll during an event" {
            database = container "PostgreSQL" "PostgreSQL database" {
                tags "Database"
            }
            application = container "Polly" "Polly application" {
                domain = component "Domain"
                infrastructure = component "Infrastructure"
                web = component "Web application"

                web -> domain "Interacts with"
                infrastructure -> domain "Enables"
                infrastructure -> web "Enables"
            }
            application -> database "Uses"
        }

        speaker -> polly "Define, run and view poll"
        attendee -> polly "Vote and view poll"
    }
    views {
        systemContext "Polly" "polly-context" {
            include speaker attendee polly
            autoLayout
        }
        container polly "polly-container" {
            include *
            autoLayout lr
        }
        component polly.application "polly-application-component" {
            include * 
            autoLayout lr
        }

        styles {
            element "Database" {
                shape Cylinder
            }
        }
        
        theme default
    }
}